package geode.handson.cui;

import java.util.Properties;

import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.util.CacheListenerAdapter;

/**
 * チャットメッセージリージョンへのイベントハンドラです。<br>
 * リージョンへ新しいメッセージが登録された場合、標準出力へ出力します。<br>
 */
public class ChatMessageListener extends CacheListenerAdapter<String, String> implements Declarable {

	/**
	 * このイベントハンドラクラスを初期化します。<br>
	 * このクラスでは特に何も行いません。<br>
	 * @param props Propertiesオブジェクト
	 */
	@Override
	public void init(Properties props) {
	}

	/**
	 * イベントとして取得したチャットメッセージを標準出力へ出力します。<br>
	 * 出力フォーマット：{Key} > {Value}<br>
	 * @param event EntryEventオブジェクト
	 */
	@Override
	public void afterCreate(EntryEvent<String, String> event) {
		System.out.println(event.getKey() + " > " + event.getNewValue());
	}
}
