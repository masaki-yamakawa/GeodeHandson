package geode.handson.cui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.query.CqAttributes;
import org.apache.geode.cache.query.CqAttributesFactory;
import org.apache.geode.cache.query.CqQuery;
import org.apache.geode.cache.query.QueryService;

/**
 * 標準入力より受け取ったメッセージをチャットリージョンへ登録します。<br>
 * リージョン登録時のキーフォーマットは、{ユーザー名}@{現在日時}@{メッセージ番号}とします。<br>
 */
public class CqChatClient {
	private static final String REGION_NAME = "ChatMessage";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	public static void main(String[] args) throws Exception {
		// キャッシュ作成
		Properties props = new Properties();
		props.setProperty("cache-xml-file", "cqclientcache.xml");
		ClientCacheFactory factory = new ClientCacheFactory(props);

		try (ClientCache cache = factory.create();
			 BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

			// cqclientcache.xmlに定義したリージョン取得
			Region<String, String> region = cache.getRegion(REGION_NAME);

			// CQ用リスナーを設定したCQ属性を作成
			CqAttributesFactory cqaFac = new CqAttributesFactory();
			cqaFac.addCqListener(new CqChatMessageListener());
			CqAttributes cqAttr = cqaFac.create();

			// CQを作成して実行
			QueryService qService = cache.getQueryService();
			CqQuery cqQuery = qService.newCq("select * from /ChatMessage", cqAttr);
//			CqQuery cqQuery = qService.newCq("select * from /ChatMessage cm where cm like '%NG Word%'", cqAttr);
			cqQuery.execute();

			System.out.println("Enter Username.");
			String user = br.readLine();

			// チャット開始 「:q」で終了
			System.out.println("Chat session start.");
			String message = null;
			int messageNo = 1;
			while ((message = br.readLine()) != null) {
				if (message.length() == 0)
					continue;
				if (message.equals(":q")) {
					region.close();
					break;
				}

				// リージョンへのメッセージ登録
				String key = String.format("%s@%s@%d", user, LocalDateTime.now().format(formatter), messageNo++);
				region.put(key, message);
			}
		}
		System.out.println("Exiting... ");

		System.exit(0);
	}
}
