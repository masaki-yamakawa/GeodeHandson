## �T�v

Embedded���[�h��CUI�`���b�g�A�v���쐬�ł́AJava�A�v���P�[�V�����̒��Ƀf�[�^�̊Ǘ����s��Geode��g�ݍ��ޕ��@���w�т܂����B
���́A�A�v���P�[�V�����ƃf�[�^�̊Ǘ����s���v���Z�X��ʁX�ɂ���N���C�A���g-�T�[�o�[�^�̕��@���w�т܂��B


## �g�p����\�[�X�A�t�@�C���̐���

�\�[�X/�t�@�C��                       | ����
------------------------------------- | --------------------------------------------
geode.handson.cui.ChatClient          | �N���C�A���g-�T�[�o�[�^�̃`���b�g�A�v����Main�N���X�ł��B<br>������Ԃł�Embedded���[�h��CUI�`���b�g�A�v���Ɠ��������ƂȂ�܂��B
geode.handson.cui.ChatMessageListener | �iEmbedded���[�h��CUI�`���b�g�A�v���쐬�Ƌ��L�j
/resources/gemfire.properties         | �iEmbedded���[�h��CUI�`���b�g�A�v���쐬�Ƌ��L�j
/resources/clientcache.xml            | �N���C�A���g�L���b�V���i���[�W�����j��ݒ肷�邽�߂̃t�@�C���ł��B<br>�f�t�H���g�ł�Embedded���[�h�Ŏg�p����cache.xml���g�p����Ă��܂����߁Agemfire.properties��cache-xml-file��ύX���邩API�ŕύX����K�v������܂��B


## ���P�[�^�[�A�L���b�V���T�[�o�[�̋N��

�N���C�A���g-�T�[�o�[�^�̃A�v���P�[�V���������s����ɂ́A���Geode�̃v���Z�X���N������K�v������܂��B
�N����gfsh�i�W�[�t�B�b�V���j���g�p���Ă����Ȃ��܂��B

``` sh
{geode_install_dir}/bin/gfsh
```

�N������Ǝ��̂悤�ȉ�ʂ��\������܂��B

``` sh    _________________________     __
   / _____/ ______/ ______/ /____/ /
  / /  __/ /___  /_____  / _____  /
 / /__/ / ____/  _____/ / /    / /
/______/_/      /______/_/    /_/    1.12.0

Monitor and Manage Apache Geode
gfsh>
```

gfsh���N��������ȉ��̃R�}���h���g�p���ă��P�[�^�[�A�L���b�V���T�[�o�[�̏��ɋN�����܂��B
�����ł�MyLocator�AMyCacheServer1�Ƃ������O�����ċN�����Ă��܂��B
���O�⃁�^�f�[�^����gfsh���N�������f�B���N�g���z���ɂ����̖��O�̃f�B���N�g�����쐬����o�͂���܂��B

``` sh
gfsh>start locator --name=MyLocator
gfsh>start server --name=MyCacheServer1
```

���K�v�ɉ�����--bind-address�p�����[�^�[�Ńo�C���h����A�h���X���w�肵�܂��B

> __�y�m�[�g�z���P�[�^�[�A�L���b�V���T�[�o�[�̒�~__
>
> ��~����ꍇ�͈ȉ��̃R�}���h�����s���܂��B
> �K���L���b�V���T�[�o�[���~���Ă��烍�P�[�^�[���~���Ă��������B
>
> stop server --name=MyCacheServer1
>
> stop locator --name=MyLocator
> 
> Windows���ł̓��P�[�^�[����~���Ȃ��ꍇ������܂��B
> ��~���Ȃ��ꍇ�̓^�X�N�}�l�[�W���[���I�������A���Y�f�B���N�g������x�폜���čēx�N�����Ă��������B


�N���X�^�[�̏�Ԃ��m�F����ɂ�gfsh��list members�R�}���h���g�p���邩�AWeb�Ǘ��R���\�[����Pulse���g�p���܂��B
Pulse��admin/admin�Ń��O�C���ł��܂��B
�����Cluster View�ŃN���X�^�[�̊Ǘ����AData Browser��OQL�ɂ��f�[�^�Q�Ƃ��\�ł��B

``` sh
gfsh>list members
gfsh>start pulse
```

![image](images/pulse.jpg)


## ���[�W�����̍쐬

���[�W�������쐬����ɂ�gfsh�ňȉ��̂悤�ɂ��č쐬���܂��B
���Ȃ݂�gfsh�̃w���v��\������ɂ�help {�R�}���h��}�Ƃ��邱�Ƃŕ\������܂��B

``` sh
gfsh>create region --name=ChatMessage --type=PARTITION
```


## �N���C�A���g�L���b�V���̍쐬

�N���C�A���g�L���b�V�����쐬����ꍇ���A�L���b�V�����쐬����ꍇ�Ɠ��l�̊��o�ōs�����Ƃ��o���܂��B
�쐬�ɂ�ClientCacheFactory���g�p���܂��B

����́AChatClient.java�̐擪�Ɏ��̂悤�ɒǉ����܂��B

``` java
Properties props = new Properties();
props.setProperty("cache-xml-file", "clientcache.xml");
ClientCacheFactory factory = new ClientCacheFactory(props);
ClientCache cache = factory.create();
```

Properties��cache-xml-file���w�肷�邱�ƂŁA�f�t�H���g��cache.xml�̒l���㏑�����āA����g�p����ݒ�t�@�C�����֕ύX���Ă��܂��B


## �N���C�A���g���[�W�����̍쐬

���Ƀ��[�W�������쐬���܂��B
�N���C�A���g���ɂ������I�Ɏg�p���郊�[�W�������쐬����K�v������܂��B
�����clientcache.xml��ChatMessage�Ƃ������O��CACHING_PROXY���[�W�������쐬���܂��B
�N���C�A���g�ƁA�����łȂ����̂̈Ⴂ�͈ȉ��̓_�ƂȂ�܂��B

- root�^�O��cache�ł͂Ȃ��Aclient-cache
- �ڑ��������킷pool�����݂���B�w�肵�Ă���̂�locator�̃z�X�g���ƃ|�[�g�B�C�x���g���擾����ꍇ��subscription-enabled="true"��ݒ肷��K�v������
- �g�p�ł��郊�[�W�����^�C�v���T�[�o�[�Ƃ͈قȂ�

``` xml
<pool name="ClientPool" subscription-enabled="true">
  <locator host="localhost" port="10334" />
</pool>

<region name="ChatMessage">
  <region-attributes refid="CACHING_PROXY" pool-name="ClientPool">
    <cache-listener>
      <class-name>geode.handson.cui.ChatMessageListener</class-name>
    </cache-listener>
  </region-attributes>
</region>
```

clientcache.xml�ō쐬�������[�W������Java�A�v���P�[�V��������擾����ɂ́A���̂悤�ɂ��܂��B
������Embedded���[�h�̏ꍇ�Ƃ܂����������ł��B

``` java
Region<String, String> region = cache.getRegion("ChatMessage");
```

## �擾����C�x���g�̓o�^

�N���C�A���g�ŃC�x���g���擾����ꍇ�ɂ̓��[�W�����ɑ΂��Ď擾�Ώۂ̃C�x���g��o�^����K�v������܂��B
�ȉ��̓L�[���[�hALL_KEYS�őS�ẴC�x���g���擾����ꍇ�̗�ł��B
���ɂ�registerInterest�𕡐�����s���A����̃L�[�̃C�x���g�݂̂��擾������AregisterInterestRegex�ŃL�[�̐��K�\�����g�p���邱�Ƃ��ł��܂��B

``` java
region.registerInterest("ALL_KEYS");
```


## �f�[�^�̓o�^

���[�W�����փf�[�^��o�^����ꍇ�͈ȉ��̂悤�ɍs���܂��B
������Embedded���[�h�̏ꍇ�Ƃ܂����������ł��B

``` java
String key = String.format("%s@%s@%d", user, LocalDateTime.now().format(formatter), messageNo++);
region.put(key, message);
```


## �C�x���g�n���h��

�C�x���g�n���h����Embedded���[�h�Ɠ������̂��g���܂��B

``` java
@Override
public void afterCreate(EntryEvent<String, String> event) {
	System.out.println(event.getKey() + " > " + event.getNewValue());
}
```


## Pulse�ɂ��f�[�^�Q��

Pulse�ł�Data Browser���g�p���ăf�[�^�̎Q�Ƃ��\�ł��B
Data Browser�ł�OQL���g�p���ăf�[�^�Q�Ƃ��s���܂��B
OQL�͔���SQL�Ɏ��Ă���ASQL��m���Ă���Ί��o�I�Ɏg���܂��B�i���[�W���������w�肷��ۂ�"/"���w�肷�邮�炢�ł��j
�������Ajoin�Ⓑ������SQL���͐���ɂ��g���Ȃ��P�[�X�����邽�߁A1���[�W�����̏����w�肪�o���郌�x���Ɗo���Ă������T�����Ƃ������Ȃ��Ȃ�܂��B

``` sql
select * from /ChatMessage cm where cm like 'a%'
```

��L��ChatMessage���[�W�����̒l��a����n�܂���̑S�Ă��Q�Ƃ���ꍇ�̗�ł��B


![image](images/databrowser.jpg)
