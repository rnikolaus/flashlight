package rnikolaus.flashlight;

import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {

	private Camera mCamera;
	private Thread blinkThread;

	/**
	 * Set the flashlight state according to the toggleButton
	 * 
	 * @param v
	 */
	public void switchFlashlight(View v) {
		toggleButtonAction();
	}

	private void toggleButtonAction() {
		ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
		flashlight(toggleButton.isChecked());
	}

	/**
	 * Switch the flashlight on or off
	 * 
	 * @param on
	 */
	public void flashlight(boolean on) {
		try {
			Parameters p = mCamera.getParameters();
			if (on) {
				if (!p.getFlashMode().equals(Parameters.FLASH_MODE_TORCH)) {
					p.setFlashMode(Parameters.FLASH_MODE_TORCH);
					mCamera.setParameters(p);
				}
			} else {
				if (p.getFlashMode().equals(Parameters.FLASH_MODE_TORCH)) {
					p.setFlashMode(Parameters.FLASH_MODE_OFF);
					mCamera.setParameters(p);
				}
			}
		} catch (RuntimeException ex) {
			// A screen orientation change during flashing morse code signals
			// might end here,
			// as the flash might be used before the camera is reinitialized
		}
	}

	/**
	 * This will flash out the pattern described by the Boolean list One
	 * timeunit has a duration of 100 ms
	 * 
	 * @param signals
	 */
	public void flashPattern(final List<Boolean> signals) {
		if (blinkThread != null) {
			blinkThread.interrupt();
		}
		blinkThread = new Thread() {
			@Override
			public void run() {
				for (Boolean signal : signals) {
					if (isInterrupted()) {// die gracefully
						break;
					}
					flashlight(signal);
					try {
						sleep(100);
					} catch (InterruptedException e) {// die gracefully
						break;
					}
				}
				toggleButtonAction();
			}

		};
		blinkThread.start();

	}

	/**
	 * Turns the content of the editText into a signal pattern and plays # it
	 * through the camera flash
	 * 
	 * @param v
	 */
	public void flash(View v) {
		EditText editText = (EditText) findViewById(R.id.editText1);
		Morse morse = new Morse();
		List<Boolean> signals = morse.messageToSignal(editText.getText()
				.toString());
		flashPattern(signals);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (mCamera == null) {
			mCamera = Camera.open();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (blinkThread != null) {
			blinkThread.interrupt();// stop blinking
		}
		if (mCamera != null) {
			mCamera.release();// release the cam to other apps
			mCamera = null;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		toggleButtonAction();
	}

}
