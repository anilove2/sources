package com.tido.music;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;

@DesignerComponent(
    version = 1,
    description = "YouTube audio extension created by Mukesh using Rush CLI",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = ""
)
@SimpleObject(external = true)
public class YouTubeAudio extends AndroidNonvisibleComponent {

    public YouTubeAudio(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Returns a hello message from the extension.")
    public String Hello() {
        return "Hello from Mukesh!";
    }
}
