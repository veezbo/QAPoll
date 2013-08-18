package com.anony.minions.qapoll;

import com.immersion.uhl.Launcher;

public class HapticLauncherManager {

	private static Launcher mLauncher = null;

	public static Launcher getInstance() {
		if (mLauncher == null) {
			mLauncher = new Launcher(QAPollContextManager.getContext());
		}

		return mLauncher;
	}
}
