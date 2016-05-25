# image-picker
Android image picker library.

# Setup

* In your `build.gradle` :

```gradle
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.nanjingboy:image-picker:1.0'
}
```

* In your `AndroidManifest.xml`:

```xml
<application>
    <activity android:name="me.tom.image.picker.activity.FolderPickerActivity"
        android:theme="@style/Theme.AppCompat.Light.NoActionBar"/>
    <activity android:name="me.tom.image.picker.activity.ImagePickerActivity"
        android:theme="@style/Theme.AppCompat.Light.NoActionBar"/>
    <activity android:name="me.tom.image.picker.activity.MultipleImagePickerActivity"
        android:theme="@style/Theme.AppCompat.Light.NoActionBar"/>
</application>

<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

#Usage

```java
import android.content.Intent;

import me.tom.image.picker.activity.FolderPickerActivity;

// start activity
Intent intent = new Intent(this, FolderPickerActivity.class);
intent.putExtra("multiple", true); // Optional, default is false
startActivityForResult(intent, FolderPickerActivity.REQUEST_IMAGE_PICKER);

//onActivityResult
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == FolderPickerActivity.REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
        ArrayList<CharSequence> images = data.getCharSequenceArrayListExtra("images");
        for (int index = 0; index < images.size(); index++) {
            Log.d("image path:", images.get(index).toString());
        }
    }
}
```

#License
MIT