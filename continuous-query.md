## �T�v

�N���C�A���g-�T�[�o�[�^��CUI�`���b�g�A�v���쐬�ł́A���[�W�����̃L�[���w�肵�Ď擾�������C�x���g���w�肵�Ă��܂��B
�N���C�A���g����̃C�x���g�擾�ł�Continuous Query�Ƃ������̂����p�\�ł��B
Continuous Query�ł�OQL�iObject Query Language�j���g�p�ł��AOQL�̌��ʂ��ς�����ꍇ�ɃC�x���g�����΂��܂��B
������g�p���邱�ƂŃ��[�W�����̃L�[�ł͂Ȃ��A�o�����[�̃I�u�W�F�N�g������̏�Ԃɕω��������ɁA�C�x���g�Ƃ��Ď擾���邱�Ƃ��o���܂��B

����ȍ~��GeodeHandsonFinal�Ŋ���������Ԃ����Ƃɐ������܂��B

## �g�p����\�[�X�A�t�@�C���̐���

�\�[�X/�t�@�C��                         | ����
--------------------------------------- | --------------------------------------------
geode.handson.cui.CqChatClient          | �N���C�A���g-�T�[�o�[�^��CQ���g�p�����`���b�g�A�v����Main�N���X�ł��B
geode.handson.cui.CqChatMessageListener | OQL���ʂ̍X�V�C�x���g���n���h������N���X�ł��B
/resources/cqclientcache.xml            | �N���C�A���g�L���b�V���i���[�W�����j��ݒ肷�邽�߂̃t�@�C���ł��B


## jar�̒ǉ�

Continuous Query���g�p����ꍇ��geode-core�Ƃ͕ʂ�jar���K�v�ƂȂ�܂��B
maven�̏ꍇ�Apom.xml�Ɏ��̂悤�Ɉˑ��֌W��ǉ����܂��B

``` xml
<dependency>
  <groupId>org.apache.geode</groupId>
  <artifactId>geode-cq</artifactId>
  <version>${geode.version}</version>
</dependency>
```


## ���P�[�^�[�A�L���b�V���T�[�o�[�̋N���A���[�W�����쐬

���P�[�^�[�A�L���b�V���T�[�o�[�̋N���A���[�W�����̍쐬�ɂ��ẮA���s���Ă��Ȃ���΁A�N���C�A���g-�T�[�o�[�^��CUI�`���b�g�A�v���쐬�Ɠ��l�Ɏ��s���Ă��������B


## CQ�p�C�x���g�n���h���iCqChatMessageListener.java�j

CQ�p�̃C�x���g�n���h������������ꍇ�́A��p�̃C���^�[�t�F�[�X����������K�v������܂��B
�L�[�x�[�X�̃C�x���g���擾����ۂ́A�f�[�^�̒ǉ��A�X�V�A�폜���A�f�[�^�̑��얈�ɃR�[���o�b�N����郁�\�b�h��������Ă��܂������ACQ�̏ꍇ��onEvent�Ƃ������\�b�h�݂̂ƂȂ�܂��B
���ɒǉ��Ȃ̂��X�V�Ȃ̂��폜�Ȃ̂��𔻒f�������ꍇ�̓R�[���o�b�N���\�b�h�œn�����CqEvent��蔻�f�\�ł��B�igetBaseOperation or getQueryOperation�j

``` java
@Override
public void onEvent(CqEvent cqEvent) {
	System.out.println(cqEvent.getKey() + " > " + cqEvent.getNewValue());
}
```

> __�y�m�[�g�z�C�x���g��ʂ̔��f__
>
> CqEvent#getQueryOperation���g�p���邱�ƂŁA�f�[�^�̒ǉ��A�X�V�A�폜���̃f�[�^���삪���f�ł��܂����A���̌��ʂ�OQL���ʂɑ΂�����̂ł��邱�Ƃɒ��ӂ��Ă��������B
> �Ⴆ��ConstantKey=100�Ƃ���Key/Value�f�[�^���������Ƃ��āACQ��"select * from /region r where r=100"�Ɠo�^���Ă����Ƃ��܂��B
> ConstantKey=100��200�֍X�V���ꂽ�ꍇ�A��������͍̂폜�C�x���g�ƒǉ��C�x���g�ł��B�i�X�V�C�x���g�ł͂���܂���j


## CQ�p�C�x���g�n���h���̓o�^

CQ�p�C�x���g�n���h����cache.xml�Ŏw�肷�邱�Ƃ��o���܂���BAPI�œo�^����K�v������܂��B

���̂���XML�ŃC�x���g�n���h���̎w�肪�Ȃ�cqclientcache.xml��API�Ŏw�肵�ăN���C�A���g�L���b�V�����쐬���Ă��܂��B

``` java
Properties props = new Properties();
props.setProperty("cache-xml-file", "cqclientcache.xml");
ClientCacheFactory factory = new ClientCacheFactory(props);
ClientCache cache = factory.create();
```

CQ�p�C�x���g�n���h����CqAttributes�N���X�ɐݒ肷��K�v������܂��B

``` java
CqAttributesFactory cqaFac = new CqAttributesFactory();
cqaFac.addCqListener(new CqChatMessageListener());
CqAttributes cqAttr = cqaFac.create();
```

�쐬����CqAttributes�ƃC�x���g�擾�p��OQL���w�肵��CqQuery���쐬���܂��B

``` java
QueryService qService = cache.getQueryService();
CqQuery cqQuery = qService.newCq("select * from /ChatMessage", cqAttr);
```

CqQuery#execute�����s���邱�ƂŃC�x���g�擾���J�n����܂��B

``` java
cqQuery.execute();
```

> __�y�m�[�g�z�C�x���g�擾�J�n���_�ł̏����f�[�^���擾�������ꍇ__
>
> CqQuery#execute��CqQuery#executeWithInitialResults�֕ύX���邱�ƂŁA�C�x���g�擾�J�n���_�ł̏����f�[�^���擾���邱�Ƃ��\�ł��B
> �����f�[�^��executeWithInitialResults�̖߂�l�ɕԋp����܂��B
