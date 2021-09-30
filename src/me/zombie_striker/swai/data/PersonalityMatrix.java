package me.zombie_striker.swai.data;

import me.zombie_striker.swai.Main;
import me.zombie_striker.swai.assignablecode.*;
import me.zombie_striker.swai.assignablecode.statements.*;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PersonalityMatrix implements Comparable<PersonalityMatrix>{

    private AssignableCode[] matrix;
    private int[] pallet;
    private int[] palletReadOnly;
    private UUID personalityUUID = UUID.randomUUID();
    private int palletsForInputs;

    private int generation = 0;

    public int getByteForField(int fieldid) {
        if (fieldid < 0 || getCode().length <= fieldid)
            return -1;
        if (!(getCode()[fieldid] instanceof AssignableField))
            return -1;
        return getByteForField((AssignableField) getCode()[fieldid]);
    }

    public int getByteForField(AssignableField field) {
        if (field == null)
            return -1;
        if (field.isReadOnly()) {
            return getPalletReadOnly()[field.getPalletIndex()];
        } else {
            return getPallet()[field.getPalletIndex()];
        }
    }

    public int getPalletsForInputs() {
        return palletsForInputs;
    }

    public PersonalityMatrix(int linesofposiblecode, int objectsInRam, int ramInputVariables, int readOnlyRamSize, boolean randomlyGenerateCode, int generation) {
        this.matrix = new AssignableCode[linesofposiblecode];
        this.pallet = new int[objectsInRam];
        this.palletsForInputs = ramInputVariables;
        this.palletReadOnly = new int[readOnlyRamSize];
        this.generation = generation;
        if (randomlyGenerateCode) {
            int startingline = generateFields();
            randomizeSomeLines(20);
            //randomizeCode(startingline);
        }
        populateRam();
    }

    public void populateRam() {
        populateRam(pallet.length-9);
    }

    public void populateRam(int numbersToFillTo) {
        for (int index = 0; index < pallet.length; index++) {
            pallet[index] = index < numbersToFillTo ? index : 0;
        }
    }

    public void randomizeCode(int startingline) {
        int lines = DataBank.seededRandom(68,startingline,generation).nextInt(matrix.length);
        personalityUUID = UUID.randomUUID();
        for (int i = startingline; i < lines; i++) {
            generateRandomNewAssignableCode(i, lines);
        }
    }

    public int generateFields() {
        int fields = 0;
        for (int i = 0; i < getPalletReadOnly().length; i++) {
            String name;
            if (getPalletReadOnly().length >= DataBank.chars.length) {
                name = DataBank.chars[i / DataBank.chars.length] + "" + DataBank.chars[i % DataBank.chars.length];
            } else {
                name = "" + DataBank.chars[i];
            }
            matrix[fields] = new AssignableField(this, "field" + name, i, true);
            fields++;
        }
        for (int i = 0; i < getPallet().length; i++) {
            String name;
            if (getPallet().length >= DataBank.chars.length*DataBank.chars.length*DataBank.chars.length) {
                name = DataBank.chars[i / (DataBank.chars.length*DataBank.chars.length*DataBank.chars.length)] + "" + DataBank.chars[(i / (DataBank.chars.length*DataBank.chars.length))% DataBank.chars.length] + ""+DataBank.chars[(i / DataBank.chars.length)% DataBank.chars.length] + ""+DataBank.chars[i % DataBank.chars.length];
            } else if (getPallet().length >= DataBank.chars.length*DataBank.chars.length) {
                name = DataBank.chars[i / (DataBank.chars.length*DataBank.chars.length)] + "" +DataBank.chars[(i / DataBank.chars.length)% DataBank.chars.length] + ""+ DataBank.chars[i % DataBank.chars.length];
            } else if (getPallet().length >= DataBank.chars.length) {
                name = DataBank.chars[i / DataBank.chars.length] + "" + DataBank.chars[i % DataBank.chars.length];
            } else {
                name = "" + DataBank.chars[i];
            }
            matrix[fields] = new AssignableField(this, "field" + name, i, false);
            fields++;
        }

        return fields;
    }

    public void generateRandomNewAssignableCode(int index, int maxlines) {
        List<Integer> fieldsReadable = new ArrayList<>();
        List<Integer> fieldsWritable = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            AssignableCode code = getCode()[i];
            if (code != null && code instanceof AssignableField) {
                fieldsReadable.add(i);
                if (!((AssignableField) code).isReadOnly()) {
                    fieldsWritable.add(i);
                }
            }
        }

        if (getCode()[index] instanceof AssignableField)
            return;

        gen:
        while (true) {
            int random = DataBank.seededRandom(124,index,generation).nextInt(14);
            if (random == 0) {
                matrix[index] = new AssignableReturn();
            } else if (fieldsReadable.size() < 2 && random == 1) {
                continue;
            } else if (random == 1) {
                int field1 = fieldsReadable.get(DataBank.seededRandom(130,index,generation).nextInt(fieldsReadable.size()));
                int field2 = fieldsReadable.get(DataBank.seededRandom(131,index,generation).nextInt(fieldsReadable.size()));
                int skippablelines = DataBank.getRandomExponentialDistribution(getCode().length - index - 1, 5) + 1;

                matrix[index] = new AssignableIfEquals(this, field1, field2, skippablelines);
            } else if (random == 2) {
                int skippablelines = DataBank.seededRandom(136,index,generation).nextInt(maxlines);

                matrix[index] = new AssignableJump(this, skippablelines, false);
            } else if ((fieldsReadable.size() < 2 || fieldsWritable.size() < 1) && random == 3) {
                continue;
            } else if (random == 3) {
                int field1 = fieldsWritable.get(DataBank.seededRandom(142,index,generation).nextInt(fieldsWritable.size()));
                int field2 = fieldsReadable.get(DataBank.seededRandom(143,index,generation).nextInt(fieldsReadable.size()));

                matrix[index] = new AssignableSetField(this, field1, true, field2);
            } else if ((fieldsReadable.size() < 2 || fieldsWritable.size() < 1) && random == 4) {
                continue;
            } else if (random == 4) {
                int field1 = fieldsWritable.get(DataBank.seededRandom(142,index,generation).nextInt(fieldsWritable.size()));
                int field2 = fieldsReadable.get(DataBank.seededRandom(143,index,generation).nextInt(fieldsReadable.size()));

                matrix[index] = new AssignableSetRandField(this, index, field1, field2);
            } else if (fieldsReadable.size() < 1 && random == 5) {
                continue;
            } else if (random == 5) {
                int field1 = fieldsReadable.get(DataBank.seededRandom(156,index,generation).nextInt(fieldsReadable.size()));

                matrix[index] = new AssignableJump(this, field1, true);
            } else if (fieldsWritable.size() < 1 && random == 6) {
                continue;
            } else if (random == 6) {
                int field1 = fieldsWritable.get(DataBank.seededRandom(162,index,generation).nextInt(fieldsWritable.size()));

                matrix[index] = new AssignableIncrement(this, field1);

            } else if (random == 7) {

                matrix[index] = null;
            } else if (fieldsWritable.size() < 1 && random == 8) {
                continue;
            } else if (random == 8) {
                int field1 = fieldsWritable.get(DataBank.seededRandom(172,index,generation).nextInt(fieldsWritable.size()));

                matrix[index] = new AssignableDecrement(this, field1);
            } else if ((fieldsReadable.size() < 2 || fieldsWritable.size() < 1) && random == 9) {
                continue;
            } else if (random == 9) {
                int field1 = fieldsWritable.get(DataBank.seededRandom(178,index,generation).nextInt(fieldsWritable.size()));
                int field2 = fieldsReadable.get(DataBank.seededRandom(179,index,generation).nextInt(fieldsReadable.size()));

                matrix[index] = new AssignableIncrementBy(this, field1, field2);
            } else if ((fieldsReadable.size() < 2 || fieldsWritable.size() < 1) && random == 10) {
                continue;
            } else if (random == 10) {
                int field1 = fieldsWritable.get(DataBank.seededRandom(185,index,generation).nextInt(fieldsWritable.size()));
                int field2 = fieldsReadable.get(DataBank.seededRandom(186,index,generation).nextInt(fieldsReadable.size()));

                matrix[index] = new AssignableDecrementBy(this, field1, field2);

            } else if (fieldsReadable.size() < 2 && random == 11) {
                continue;
            } else if (random == 11) {
                int field1 = fieldsReadable.get(DataBank.seededRandom(193,index,generation).nextInt(fieldsReadable.size()));
                int field2 = fieldsReadable.get(DataBank.seededRandom(194,index,generation).nextInt(fieldsReadable.size()));
                int skippablelines = DataBank.getRandomExponentialDistribution(getCode().length - index - 1, 5) + 1;

                matrix[index] = new AssignableIfLessThan(this, field1, field2, skippablelines);

            } else if (random == 12) {
                int skippablelines = DataBank.seededRandom(200,index,generation).nextInt(maxlines);

                matrix[index] = new AssignableGoSub(skippablelines);
            }
            break;
        }
    }


    public AssignableCode[] getCode() {
        return matrix;
    }

    public int run() {
        return run(false, -1);
    }

    public int run(boolean debug, int maxDebugEntries) {
        int linesRan = 0;
        int debugentries = 0;


        int stackIndex = 0;
        int[] stack = new int[100];

        for (int i = 0; i < matrix.length; i++) {
            if (linesRan >= 10000) {
                //System.out.println(DataBank.ANSI_RED + "[Halting Program as it went over the 10k lines limit]" + DataBank.ANSI_RESET);
                break;
            }
            linesRan++;
            AssignableCode code = matrix[i];
            if (code != null) {
                if (debug) {
                    if (debugentries < maxDebugEntries) {
                        System.out.println(i + " " + code.toString());
                        debugentries++;
                    }
                }
                if (code instanceof AssignableStatement) {
                    ((AssignableStatement) code).call();
                }else if (code instanceof AssignableGoSub) {
                    if (stackIndex >= 100) {
                        break;
                    }
                    stack[stackIndex] = i;
                    stackIndex++;
                    i = ((AssignableGoSub) code).getLineToJumpTo()-1;
                    continue;
                }else if(code instanceof AssignableReturn){
                    if(stackIndex > 0) {
                        stackIndex--;
                        i = stack[stackIndex];
                        continue;
                    }else{
                        break;
                    }
                }else if (code instanceof AssignableJump) {
                    i = ((AssignableJump) code).getLineToSkipTo()-1;
                    continue;
                }else if (code instanceof AssignableIfEquals) {
                    if (!((AssignableIfEquals) code).compare()) {
                        i += ((AssignableIfEquals) code).getSkippableLines() - 1;
                        continue;
                    }
                }else if (code instanceof AssignableIfLessThan) {
                    if (!((AssignableIfLessThan) code).compare()) {
                        i += ((AssignableIfLessThan) code).getSkippableLines() - 1;
                        continue;
                    }
                }
            }
        }
        return linesRan;
    }

    public UUID getUUID() {
        return personalityUUID;
    }

    public int[] getPallet() {
        return pallet;
    }

    public int[] getPalletReadOnly() {
        return palletReadOnly;
    }

    public PersonalityMatrix clone() {
        PersonalityMatrix matrix = new PersonalityMatrix(getCode().length, pallet.length, palletsForInputs, palletReadOnly.length, false,  generation);
        for (int i = 0; i < getCode().length; i++) {
            matrix.getCode()[i] = getCode()[i] == null ? null : getCode()[i].clone(matrix);
        }
        matrix.populateRam();
        matrix.personalityUUID = personalityUUID;
        return matrix;
    }

    public void randomizeSomeLines(double percetnage) {
        personalityUUID = UUID.randomUUID();
        for (int i = 0; i < getCode().length; i++) {
            int chance = DataBank.seededRandom(generation,i+5,getCode().length).nextInt((int) (100.0 / percetnage));
            if (chance == 0) {
                generateRandomNewAssignableCode(i, getCode().length);
            }
        }
    }

    public int getGeneration() {
        return generation;
    }
    public void setGeneration(int generation){
        this.generation = generation;
    }

    @Override
    public int compareTo(PersonalityMatrix o) {
        return getGeneration()-o.getGeneration();
    }

    public static PersonalityMatrix load(File savefile){
        try {
            String data = DataBank.readFile(new FileInputStream(savefile));
            String[] text = data.split("\n");
            String[] codelines = new String[text.length-4];

            int ram = Integer.parseInt(text[0]);
            int input = Integer.parseInt(text[1]);
            int readonly = Integer.parseInt(text[2]);
            int gen = Integer.parseInt(text[3]);

            for(int i = 0; i < codelines.length; i++){
                codelines[i]=text[i+4];
            }
            return load(codelines,ram,input,readonly,gen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PersonalityMatrix load(String[] lines, int ram, int input, int readonly, int gen){
        PersonalityMatrix matrix = new PersonalityMatrix(lines.length,ram,input,readonly,false,gen);
        for(int i = 0; i < lines.length; i++){
            if(lines[i].toUpperCase().startsWith("RETURN")){
                matrix.getCode()[i] = new AssignableReturn();
            }else if(lines[i].toUpperCase().startsWith("JUMPTO")){
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int jumpto = -1;
                try{
                    jumpto = Integer.parseInt(data[0]);
                }catch(Exception e3){}
                matrix.getCode()[i] = new AssignableJump(matrix,jumpto,false);
            }else if(lines[i].toUpperCase().startsWith("JUMP")){
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int jumpto = -1;
                try{
                    jumpto = Integer.parseInt(data[0]);
                }catch(Exception e3){}
                matrix.getCode()[i] = new AssignableJump(matrix,jumpto,true);
            }else if(lines[i].toUpperCase().startsWith("IFLESSTHAN")){
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                int jumpto = -1;
                try{
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                    jumpto = Integer.parseInt(data[2]);
                }catch(Exception e3){}
                matrix.getCode()[i] = new AssignableIfLessThan(matrix,f1,f2,jumpto);
            }else if(lines[i].toUpperCase().startsWith("IFEQUALS")){
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                int jumpto = -1;
                try{
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                    jumpto = Integer.parseInt(data[2]);
                }catch(Exception e3){}
                matrix.getCode()[i] = new AssignableIfEquals(matrix,f1,f2,jumpto);
            }else if(lines[i].toUpperCase().startsWith("GOSUB")){
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int jumpto = -1;
                try{
                    jumpto = Integer.parseInt(data[0]);
                }catch(Exception e3){}
                matrix.getCode()[i] = new AssignableGoSub(jumpto);
            }else if(lines[i].toUpperCase().startsWith("RAND")){
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                int magicnumber = i;
                try{
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                }catch(Exception e3){}
                matrix.getCode()[i] = new AssignableSetRandField(matrix,magicnumber,f1,f2);
            }else if(lines[i].toUpperCase().startsWith("FIELDRO")){
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                String name = null;
                try{
                    name = data[0];
                    f1 = Integer.parseInt(data[1]);
                }catch(Exception e3){}
                matrix.getCode()[i] = new AssignableField(matrix,name,f1,true);
            }else if(lines[i].toUpperCase().startsWith("FIELD")){
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                String name = null;
                try{
                    name = data[0];
                    f1 = Integer.parseInt(data[1]);
                }catch(Exception e3){}
                matrix.getCode()[i] = new AssignableField(matrix,name,f1,false);
            }else if(lines[i].toUpperCase().startsWith("SET")){
            }else if(lines[i].toUpperCase().startsWith("SETF")){
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                try{
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                }catch(Exception e3){}
                matrix.getCode()[i] = new AssignableSetField(matrix,f1,true,f2);
            }else if(lines[i].toUpperCase().startsWith("SET")){
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                try{
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                }catch(Exception e3){}
                matrix.getCode()[i] = new AssignableSetField(matrix,f1,false,f2);
            }else if(lines[i].toUpperCase().startsWith("INCBY")){
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                try{
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                }catch(Exception e3){}
                matrix.getCode()[i] = new AssignableIncrementBy(matrix,f1,f2);
            }else if(lines[i].toUpperCase().startsWith("DECBY")){
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                try{
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                }catch(Exception e3){}
                matrix.getCode()[i] = new AssignableDecrementBy(matrix,f1,f2);
            }else if(lines[i].toUpperCase().startsWith("DEC")){
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                try{
                    f1 = Integer.parseInt(data[0]);
                }catch(Exception e3){}
                matrix.getCode()[i] = new AssignableDecrement(matrix,f1);
            }else if(lines[i].toUpperCase().startsWith("INC")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableIncrement(matrix, f1);
            }else if(lines[i]==null || lines[i].equalsIgnoreCase("NULL")){
            }else{
                System.out.println("FAILED TO READ : "+lines[i]);
            }
        }
        return matrix;
    }

    public void saveTo(File writeTo) {
        System.out.println("Saving PersonalityMatrix "+getUUID().toString()+" to "+writeTo.getPath());
        try {
            FileWriter fw = new FileWriter(writeTo);
            fw.write(pallet.length+"\n");
            fw.write(palletsForInputs+"\n");
            fw.write(palletReadOnly.length+"\n");
            fw.write(generation+"\n");
            for(int line2 = 0; line2 < getCode().length; line2++){
                if(getCode()[line2]==null){
                    fw.write("NULL");
                }else {
                    fw.write(getCode()[line2].toString());
                }
                fw.write("\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
