## ���v���P�[�V�����ƃp�[�e�B�V�����̃C�x���g

Embedded���[�h��CUI�`���b�g�A�v���ł�REPLICATE���[�W�������g�p���܂����B
�����PARTITION�ɕύX����Ɛ��������삷��ł��傤���H


## �p�[�e�B�V�����ւ̕ύX

cache.xml��ChatMessage�̃��[�W�����^�C�v��REPLICATE����PARTITION�֕ύX���܂��B

``` xml
<region name="ChatMessage">
  <region-attributes refid="PARTITION">
    <cache-listener>
      <class-name>geode.handson.cui.ChatMessageListener</class-name>
    </cache-listener>
  </region-attributes>
</region>
```

���̕ύX��`���b�g�A�v���𓮂����Ɓc
���悻2���1�񂵂��\������Ȃ��Ȃ�܂����B


## �p�[�e�B�V�������[�W�����̃C�x���g

Embedded���[�h��PARTITION�̃C�x���g����������ꍇ�́A�f�t�H���g�ł̓v���C�}���[�f�[�^������Ƃ��낾���ł����C�x���g���������Ȃ����Ƃɒ��ӂ���K�v������܂��B
�ꌩ�f�����b�g�̂悤�ɂ��v���܂����A���̃N���X�^�[����1�ӏ��ł����C�x���g���������Ȃ������𗘗p���ăA�v���P�[�V�������쐬�邱�Ƃ��\�ł��B

__�y�m�[�g�z�N���C�A���g����̃C�x���g����__
>
> �N���C�A���g����C�x���g���擾����ꍇ�͑Ώۂ��p�[�e�B�V�����ł����Ă��S�ẴC�x���g���擾���邱�Ƃ��\�ł��B
> �������͑S�Ăł͂Ȃ��A�N���C�A���g��registerInterest/registerInterestRegex�������̂̂ݔ�������B


## �p�[�e�B�V�������[�W�����őS�C�x���g���擾�������Ƃ�

�p�[�e�B�V�������[�W�����ł��S�ẴC�x���g���擾�������ꍇ��cache.xml��"subscription-attributes"��ǉ����܂��B

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
