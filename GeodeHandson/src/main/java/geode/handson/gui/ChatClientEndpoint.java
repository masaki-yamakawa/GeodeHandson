package geode.handson.gui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.geode.cache.CacheClosedException;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.InterestResultPolicy;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.util.CacheListenerAdapter;

public class ChatClientEndpoint {
	private static final String REGION_NAME = "ChatMessage";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	private final String userName;
	private final String roomName;
	private ClientCache cache;
	private final AtomicLong seq = new AtomicLong(0);
	private MessageHandler messageHandler;

	public ChatClientEndpoint(String host, Integer port, String userId, String roomName) {
		try {
			this.userName = userId;
			this.roomName = roomName;

			try {
				cache = ClientCacheFactory.getAnyInstance();
			} catch (CacheClosedException e) {
				System.out.println("Ignore CacheClosedException");
				Properties props = new Properties();
				props.setProperty("cache-xml-file", "guiclientcache.xml");
				ClientCacheFactory factory = new ClientCacheFactory(props).addPoolLocator(host, port).setPoolSubscriptionEnabled(true);
				cache = factory.create();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void addMessageHandler(final MessageHandler msgHandler) {
		messageHandler = msgHandler;
		Region<String, String> region = cache.getRegion(REGION_NAME);
		if (region == null) {
			ClientRegionFactory<String, String> rgnFac = cache.createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY);
			region = rgnFac.addCacheListener(new ChatMessageListener()).create(REGION_NAME);
			region.registerInterest("ALL_KEYS", InterestResultPolicy.KEYS_VALUES);
		}
	}

	public void sendMessage(final String message) {
		Region<String, String> region = cache.getRegion(REGION_NAME);
		String key = String.format("%s@%s@%d", userName, LocalDateTime.now().format(formatter), seq.getAndIncrement());
		region.put(key, message);
	}

	static interface MessageHandler {
		void handleMessage(String message);
	}

	private class ChatMessageListener extends CacheListenerAdapter<String, String> {

		/**
		 * イベントとして取得したチャットメッセージを標準出力へ出力します。<br>
		 * 出力フォーマット：{Key} > {Value}<br>
		 * @param event EntryEventオブジェクト
		 */
		@Override
		public void afterCreate(EntryEvent<String, String> event) {
			System.out.println(event.getKey() + " > " + event.getNewValue());
			if (messageHandler != null) {
				messageHandler.handleMessage(event.getKey() + System.getProperty("line.separator") + event.getNewValue());
			}
		}
	}
}