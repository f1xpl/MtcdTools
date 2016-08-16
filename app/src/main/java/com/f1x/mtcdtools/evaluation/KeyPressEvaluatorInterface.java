package com.f1x.mtcdtools.evaluation;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public interface KeyPressEvaluatorInterface {
    void evaluateLaunchInput(String packageName);
    void evaluateMediaInput(int androidKeyCode);
    void evaluateModeInput();
}
