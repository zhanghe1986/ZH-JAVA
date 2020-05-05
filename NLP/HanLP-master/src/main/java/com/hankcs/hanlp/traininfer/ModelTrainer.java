package com.hankcs.hanlp.traininfer;


import com.hankcs.hanlp.model.perceptron.CWSTrainer;
import com.hankcs.hanlp.model.perceptron.NERTrainer;
import com.hankcs.hanlp.model.perceptron.POSTrainer;
import com.hankcs.hanlp.model.perceptron.PerceptronTrainer;

public class ModelTrainer {

    public static void main(String[] args)
    {
        try {
            PerceptronTrainer trainer = new POSTrainer();
            trainer.train("data/test/pku98/199801.txt", Config.POS_MODEL_FILE);
        } catch (Exception e) {}

        try {
            PerceptronTrainer trainer = new NERTrainer();
            trainer.train("data/test/pku98/199801.txt", Config.NER_MODEL_FILE);
        } catch (Exception e) {}

        try {
            PerceptronTrainer trainer = new CWSTrainer();
            trainer.train("data/test/pku98/199801.txt", Config.CWS_MODEL_FILE);
        } catch (Exception e) {}


    }
}
