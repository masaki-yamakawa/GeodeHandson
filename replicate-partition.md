## レプリケーションとパーティションのイベント

EmbeddedモードのCUIチャットアプリではREPLICATEリージョンを使用しました。
これをPARTITIONに変更すると正しく動作するでしょうか？


## パーティションへの変更

cache.xmlのChatMessageのリージョンタイプをREPLICATEからPARTITIONへ変更します。

``` xml
<region name="ChatMessage">
  <region-attributes refid="PARTITION">
    <cache-listener>
      <class-name>geode.handson.cui.ChatMessageListener</class-name>
    </cache-listener>
  </region-attributes>
</region>
```

この変更後チャットアプリを動かすと…
およそ2回に1回しか表示されなくなりました。


## パーティションリージョンのイベント

EmbeddedモードでPARTITIONのイベントを処理する場合は、デフォルトではプライマリーデータがあるところだけでしかイベントが発生しないことに注意する必要があります。
一見デメリットのようにも思えますが、このクラスター内の1箇所でしかイベントが発生しない特性を利用してアプリケーションを作成ることも可能です。

__【ノート】クライアントからのイベント処理__
>
> クライアントからイベントを取得する場合は対象がパーティションであっても全てのイベントを取得することが可能です。
> 正しくは全てではなく、クライアントがregisterInterest/registerInterestRegexしたもののみ発生する。


## パーティションリージョンで全イベントを取得したいとき

パーティションリージョンでも全てのイベントを取得したい場合はcache.xmlへ"subscription-attributes"を追加します。

``` xml
<region name="ChatMessage">
  <region-attributes refid="PARTITION">
    <subscription-attributes interest-policy="all" />
    <cache-listener>
      <class-name>geode.handson.cui.ChatMessageListener</class-name>
    </cache-listener>
  </region-attributes>
</region>
```
