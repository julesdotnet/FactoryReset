package jules.factoryreset.sfxhandling;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer {
	public SoundPlayer() {
		loadAllSoundEffects();
	}
	
	private static Map<String, Clip> soundMap = new HashMap<>();
	
	public void loadSound(String name, String filePath) {
	    try (InputStream audioSrc = getClass().getResourceAsStream(filePath);
	         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioSrc)) {

	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        soundMap.put(name, clip);

	    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | NullPointerException e) {
	        e.printStackTrace();
	        System.out.println("ERROR: An exception occurred!");
	    }
	}
	
	public void playSound(String name) {
		Clip clipToPlay;
		if((clipToPlay = soundMap.get(name)) != null) {
			clipToPlay.setFramePosition(0);
			clipToPlay.start();
		}
	}
	
	public static void stopSound(String name) {
		Clip clipToPlay;
		if((clipToPlay = soundMap.get(name)) != null) {
			clipToPlay.stop();
		}
	}
	
	public static void setVolume(float volume) {
    	for (Clip clip : soundMap.values()) {
    		if(clip != null) {
    			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = (float) (Math.log(volume / 100.0) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
    		}
    	}
    }
	
	public void loadAllSoundEffects() {
		loadSound("laserRayShot", "/sfx/laser-ray-shot.wav");
		loadSound("bgtrack1", "/sfx/density_time_maze.wav");
	}
}
