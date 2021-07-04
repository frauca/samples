package frauca.backend.sampler;

import frauca.backend.guesser.Riddle;

public class RiddleSampler {

    public static Riddle sample(){
        return Riddle.builder()
                .min(0)
                .max(10)
                .name("sample")
                .build();
    }
}
