package geode.handson.cui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;

/**
 * 標準入力より受け取ったメッセージをチャットリージョンへ登録します。<br>
 * リージョン登録時のキーフォーマットは、{ユーザー名}@{現在日時}@{メッセージ番号}とします。<br>
 */
public class P2PChat {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	public static void main(String[] args) throws Exception {

		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

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
					break;
				}

			}
		}
		System.out.println("Exiting... ");

		System.exit(0);
	}
}
