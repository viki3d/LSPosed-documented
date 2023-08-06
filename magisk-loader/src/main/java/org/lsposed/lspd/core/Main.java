/*
 * This file is part of LSPosed.
 *
 * LSPosed is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LSPosed is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LSPosed.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2022 LSPosed Contributors
 */

package org.lsposed.lspd.core;

import android.os.IBinder;
import android.os.Process;

import org.lsposed.lspd.service.ILSPApplicationService;
import org.lsposed.lspd.util.ParasiticManagerHooker;
import org.lsposed.lspd.util.Utils;
import org.lsposed.lspd.BuildConfig;

public class Main {

    public static void forkCommon(boolean isSystem, String niceName, IBinder binder) {
        Utils.logI("[magisk-loader]/Main.forkCommon():  isSystem=" + isSystem + " niceName=" + niceName);
        // [core]/org.lsposed.lspd.core.Startup
        // DEFAULT_MANAGER_PACKAGE_NAME = /build.gradle.gtk/defaultManagerPackageName = "org.lsposed.manager"
        // MANAGER_INJECTED_PKG_NAME    = /build.gradle.gtk/injectedPackageName = "com.android.shell"
        // MANAGER_INJECTED_UID         = /build.gradle.gtk/injectedPackageUid = 2000
        Startup.initXposed(isSystem, niceName, ILSPApplicationService.Stub.asInterface(binder));
        if ((niceName.equals(BuildConfig.MANAGER_INJECTED_PKG_NAME) || niceName.equals(BuildConfig.DEFAULT_MANAGER_PACKAGE_NAME))
                && ParasiticManagerHooker.start()) {
            Utils.logI("Loaded manager, skipping next steps");
            return;
        }
        Utils.logI("Loading xposed for " + niceName + "/" + Process.myUid());
        Startup.bootstrapXposed();
    }
}
