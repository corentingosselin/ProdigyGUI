package fr.cocoraid.prodigygui.config;

import java.io.File;
import java.util.Arrays;

public class CoreConfig extends Skyoconfig {

    @ConfigOptions(name = "language")
    public String language = "english";

    @ConfigOptions(name = "button.previous.texture")
    public String button_previous_texture = "ZjI1OTliZDk4NjY1OWI4Y2UyYzQ5ODg1MjVjOTRlMTlkZGQzOWZhZDA4YTM4Mjg0YTE5N2YxYjcwNjc1YWNjIn19fQ==";
    @ConfigOptions(name = "button.next.texture")
    public String button_next_texture = "YzJmOTEwYzQ3ZGEwNDJlNGFhMjhhZjZjYzgxY2Y0OGFjNmNhZjM3ZGFiMzVmODhkYjk5M2FjY2I5ZGZlNTE2In19fQ==";

    public CoreConfig(final File configFile) {
        super(configFile, Arrays.asList(""));
    }

}