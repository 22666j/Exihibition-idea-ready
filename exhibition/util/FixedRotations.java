package exhibition.util;


public class FixedRotations implements MinecraftUtil{
    private float yaw;
    private float pitch;
    private float lastYaw;
    private float lastPitch;

    public FixedRotations(float startingYaw, float startingPitch) {
        this.lastYaw = this.yaw = startingYaw;
        this.lastPitch = this.pitch = startingPitch;
    }

    public void updateRotations(float requestedYaw, float requestedPitch) {
        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;
        float gcd = RotationUtils.getGCD();
        float yawDiff = requestedYaw - this.yaw;
        float pitchDiff = requestedPitch - this.pitch;
        float fixedYawDiff = yawDiff - yawDiff % gcd;
        float fixedPitchDiff = pitchDiff - pitchDiff % gcd;
        this.yaw += fixedYawDiff;
        this.pitch += fixedPitchDiff;
        this.pitch = Math.max(-90.0f, Math.min(90.0f, this.pitch));
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getLastYaw() {
        return this.lastYaw;
    }

    public float getLastPitch() {
        return this.lastPitch;
    }
}
