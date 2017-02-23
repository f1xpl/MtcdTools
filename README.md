# MtcdTools

As of version 1.3, MtcdTools is even more powerful.

#Available features:

## Action
### Key action
Use this action to simulate a press of an android media key (e.g. play, next, pause).

* **List of available key codes to simulate**:
  * KEYCODE_MEDIA_FAST_FORWARD
  * KEYCODE_MEDIA_NEXT
  * KEYCODE_MEDIA_PAUSE
  * KEYCODE_MEDIA_PLAY
  * KEYCODE_MEDIA_PLAY_PAUSE
  * KEYCODE_MEDIA_PREVIOUS
  * KEYCODE_MEDIA_REWIND
  * KEYCODE_MEDIA_STOP

### Launch action
Use this action to launch an application installed on the system. MtcdTools will prepare a list of installed applications for you.

### Start activity action
Similar feature is available in Tasker. It is useful to launch an application in a non standard way, e.g. displays specific activity accordingly to provided data URI. More information how to deal with intent are available on [Android Developer site](https://developer.android.com/reference/android/content/Intent.html).

### Broadcast intent action
Similar feature is available in Tasker. Use this action to broadcast an intent through the system. Some applications are using this mechanism to communicate with other apps. E.g. MTC devices are using this mechanism to notify about key press. More information how to deal with broadcast are available on [Android Developer site](https://developer.android.com/guide/components/broadcasts.html).

## Actions sequence

You can group previously created actions in sequences. Actions are stored and executed in order of Addition. It is possible to manipulate the delay of execution of an action in sequence.

### Example usage:
Let's assume you defined actions "stop", "play", "my music player" and "my podcast player". Now you want to switch between your players and start playback automatically. Just declare two sequences with actions in a specific order:

#### [Sequence 1]
[Stop] *// it should stop any active playback*

[My music player] *// it will launch your music player*

[Play] *// it will trigger playback of you music player launched in previous step. You can set some delay due to poor performance of MTC devices*

#### [Sequence 2]
[Stop] *// it should stop any active playback*

[My podcast player] *// it will launch your podcast player*

[Play] *// it will trigger playback of you podcast player launched in previous step. You can set some delay due to poor performance of MTC devices*

Then you can bind your action sequences with the desired key sequence or add it to the actions list and automate playback of your music players.

## Actions list
Use it to group your actions and action sequences. You can define key sequence to scroll the list up and down. In settings you can set a time after which highlighted item is executed. This feature is more powerful than regular "mode" because you can fully control what action will be executed. You do not need to stick to the defined "mode" order and harm your devices by launching of unnecessary applications.

## MODE list
As of version 1.4, the MODE functionality has been added. To the MODE list you can add actions and action sequences. In contrast to version 1.2, you can define as many MODE lists as you want.

## Keys sequence
As of version 1.3 you are able to bind your defined objects (actions, action sequences, action lists, MODE lists) not only to single key but also to keys sequence. It means that you can press any variation of hard keys (device keys and steering wheel keys) to execute your defined action.

## Voice control
> If you want to make hands-free calls, consider to use [MtcDialer](https://github.com/f1xpl/MtcDialer).

As of version 1.3 you are able to control your device using voice. You can say your defined actions or action sequences names, then MtcdTools will execute it for you with provided order. It is possible to execute a single action or action sequence, or mix them together using "concatenation word" that you can define in Settings. Voice control is localized. Input language corresponds to the local language of your device. If "Google Now" supports your language, then MtcdTools will support it as well.

### *In order to use the voice control feature, you have to define a "start activity" action with below parameters and bind it with desired keys sequence:*

> Class name: com.f1x.mtcdtools.activities.VoiceDispatchActivity

> Intent package: com.f1x.mtcdtools

> Flags: 813760516

## Settings
### Delay of execution action from the list
>define a time after which highlighted item from the actions list is executed.

### Voice command execution delay
>define a time after which MtcdTools will start processing of provided voice input. A parameter has been introduced due to poor performance of our devices. It is helpful when you want to execute e.g. a playback control action ("play", "pause", "next" etc.). "Google Now" activity will interrupt the playback (grabs the audio focus) during speech recognition and needs some time to resume it. Sometimes it can interfere with playback control actions.

### Key press speed
>define how long MtcdTools will wait to collect next key press to the sequence.

### Actions voice delimiter (e.g. X then Y)
>Word used by MtcdTools to extract item names from provided voice input. (e.g. "my music player" <DEFINED WORD> "play").

## Lollipop limitations
Due to Lollipop limitations, it is possible that triggering of the voice dispatch activity will bring MtcdTools application to front. To avoid this behavior, close MtcdTools using "recent" menu.

## Useful actions

### *Enable MtcdTools voice control:*

> Class name: com.f1x.mtcdtools.activities.VoiceDispatchActivity

> Intent package: com.f1x.mtcdtools

> Flags: 813760516

### *Display your Spotify's "starred" songs:*
> Intent action: android.intent.action.VIEW

> Intent Data: spotify:collection:tracks

> Intent Flags: 268435456

### *Launch Google Maps in "driving mode":*
> Intent action: android.intent.action.VIEW

> Intent Data: google.navigation:/?free=1&mode=d&entry=fnls

> Intent Flags: 268435456

### *Launch Google Voice Assistant (Ok, Google)*
> Intent action: android.intent.action.VOICE_COMMAND

> Intent Flags: 268435456