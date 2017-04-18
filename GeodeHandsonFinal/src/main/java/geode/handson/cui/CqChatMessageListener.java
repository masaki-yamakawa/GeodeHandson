package geode.handson.cui;

import org.apache.geode.cache.query.CqEvent;
import org.apache.geode.cache.util.CqListenerAdapter;

/**
 * チャットメッセージリージョンへのCQ用イベントハンドラです。<br>
 * リージョンへ新しいメッセージが登録された場合、標準出力へ出力します。<br>
 */
public class CqChatMessageListener extends CqListenerAdapter {

	/**
	 * イベントとして取得したチャットメッセージを標準出力へ出力します。<br>
	 * 出力フォーマット：{Key} > {Value}<br>
	 *
	 * @param event EntryEventオブジェクト
	 */
	@Override
	public void onEvent(CqEvent cqEvent) {
		System.out.println(cqEvent.getKey() + " > " + cqEvent.getNewValue());
	}
}
