package com.ikamaru.capacitor.hms.userdetect.api;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "UserDetect")
public class UserDetectPlugin extends Plugin {

    private UserDetect implementation;

    @Override
    public void load() {
        super.load();
        implementation= new UserDetect(this.bridge.getActivity());
    }
    @PluginMethod
    public void detectUser(PluginCall call) {
        implementation.detectUser(call);
    }
}
