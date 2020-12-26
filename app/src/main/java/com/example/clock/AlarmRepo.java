package com.example.clock;

import android.content.Context;

import com.example.clock.models.Alarm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AlarmRepo {
    private static String filename = "alarms";

    public static void save(Alarm alarm, Context context) {
        String filename = "alarms";
        String fileContents = "";
        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        }
        catch(FileNotFoundException e) {

        }
        catch(IOException e) {

        }
    }

    public static List<Alarm> findAll(Context context) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {


            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
            return new ArrayList<>();
        } finally {
            String contents = stringBuilder.toString();
        }
        return new ArrayList<>();
    }
}
