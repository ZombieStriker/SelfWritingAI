package me.zombie_striker.swai.data;

import me.zombie_striker.swai.Main;
import me.zombie_striker.swai.assignablecode.*;
import me.zombie_striker.swai.assignablecode.statements.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PersonalityMatrix {

    private AssignableCode[] matrix;
    private int[] pallet;
    private int[] palletReadOnly;
    private UUID personalityUUID = UUID.randomUUID();
    private int palletsForInputs;

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

    public PersonalityMatrix(int linesofposiblecode, int objectsInRam, int ramInputVariables, int readOnlyRamSize, boolean generatePersonality) {
        this.matrix = new AssignableCode[linesofposiblecode];
        this.pallet = new int[objectsInRam];
        this.palletReadOnly = new int[readOnlyRamSize];
        this.palletsForInputs = ramInputVariables;
        int startingline = generateFields();
        if (generatePersonality)
            randomizeCode(startingline);
        populateRam();
    }

    public void populateRam() {
        populateRam(pallet.length-9);
    }

    public void populateRam(int numbersToFillTo) {
        int index = 0;
        for (byte i = 0; index < pallet.length; index++) {
            pallet[index] = i < numbersToFillTo ? i : 0;
            i++;
        }
    }

    public void randomizeCode(int startingline) {
        int lines = ThreadLocalRandom.current().nextInt(matrix.length);
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
            int random = ThreadLocalRandom.current().nextInt(13);
            if (random == 0) {
                matrix[index] = new AssignableReturn();
            } else if (fieldsReadable.size() < 2 && random == 1) {
                continue;
            } else if (random == 1) {
                int field1 = fieldsReadable.get(ThreadLocalRandom.current().nextInt(fieldsReadable.size()));
                int field2 = fieldsReadable.get(ThreadLocalRandom.current().nextInt(fieldsReadable.size()));
                int skippablelines = DataBank.getRandomExponentialDistribution(getCode().length - index - 1, 5) + 1;

                matrix[index] = new AssignableIfEquals(this, field1, field2, skippablelines);
            } else if (random == 2) {
                int skippablelines = ThreadLocalRandom.current().nextInt(maxlines);

                matrix[index] = new AssignableJump(this, skippablelines, false);
            } else if ((fieldsReadable.size() < 2 || fieldsWritable.size() < 1) && random == 3) {
                continue;
            } else if (random == 3) {
                int field1 = fieldsWritable.get(ThreadLocalRandom.current().nextInt(fieldsWritable.size()));
                int field2 = fieldsReadable.get(ThreadLocalRandom.current().nextInt(fieldsReadable.size()));

                matrix[index] = new AssignableSetField(this, field1, true, field2);
            } else if (fieldsReadable.size() < 1 && random == 4) {
                continue;
            } else if (random == 4) {
                int field1 = fieldsReadable.get(ThreadLocalRandom.current().nextInt(fieldsReadable.size()));

                matrix[index] = new AssignablePostField(this, field1);

            } else if (fieldsReadable.size() < 1 && random == 5) {
                continue;
            } else if (random == 5) {
                int field1 = fieldsReadable.get(ThreadLocalRandom.current().nextInt(fieldsReadable.size()));

                matrix[index] = new AssignableJump(this, field1, true);
            } else if (fieldsWritable.size() < 1 && random == 6) {
                continue;
            } else if (random == 6) {
                int field1 = fieldsWritable.get(ThreadLocalRandom.current().nextInt(fieldsWritable.size()));

                matrix[index] = new AssignableIncrement(this, field1);

            } else if (random == 7) {

                matrix[index] = null;
            } else if (fieldsWritable.size() < 1 && random == 8) {
                continue;
            } else if (random == 8) {
                int field1 = fieldsWritable.get(ThreadLocalRandom.current().nextInt(fieldsWritable.size()));

                matrix[index] = new AssignableDecrement(this, field1);
            } else if ((fieldsReadable.size() < 2 || fieldsWritable.size() < 1) && random == 9) {
                continue;
            } else if (random == 9) {
                int field1 = fieldsWritable.get(ThreadLocalRandom.current().nextInt(fieldsWritable.size()));
                int field2 = fieldsReadable.get(ThreadLocalRandom.current().nextInt(fieldsReadable.size()));

                matrix[index] = new AssignableIncrementBy(this, field1, field2);
            } else if ((fieldsReadable.size() < 2 || fieldsWritable.size() < 1) && random == 10) {
                continue;
            } else if (random == 10) {
                int field1 = fieldsWritable.get(ThreadLocalRandom.current().nextInt(fieldsWritable.size()));
                int field2 = fieldsReadable.get(ThreadLocalRandom.current().nextInt(fieldsReadable.size()));

                matrix[index] = new AssignableDecrementBy(this, field1, field2);

            } else if (fieldsReadable.size() < 2 && random == 11) {
                continue;
            } else if (random == 11) {
                int field1 = fieldsReadable.get(ThreadLocalRandom.current().nextInt(fieldsReadable.size()));
                int field2 = fieldsReadable.get(ThreadLocalRandom.current().nextInt(fieldsReadable.size()));
                int skippablelines = DataBank.getRandomExponentialDistribution(getCode().length - index - 1, 5) + 1;

                matrix[index] = new AssignableIfLessThan(this, field1, field2, skippablelines);

            } else if (random == 12) {
                int skippablelines = ThreadLocalRandom.current().nextInt(maxlines);

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
                }
                if (code instanceof AssignableGoSub) {
                    if (stackIndex >= 100) {
                        break;
                    }
                    stack[stackIndex] = i;
                    stackIndex++;
                    i = ((AssignableGoSub) code).getLineToJumpTo()-1;
                    continue;
                }
                if(code instanceof AssignableReturn){
                    if(stackIndex > 0) {
                        stackIndex--;
                        i = stack[stackIndex];
                        continue;
                    }else{
                        break;
                    }
                }
                if (code instanceof AssignableJump) {
                    i = ((AssignableJump) code).getLineToSkipTo()-1;
                    continue;
                }
                if (code instanceof AssignableIfEquals) {
                    if (!((AssignableIfEquals) code).compare()) {
                        i += ((AssignableIfEquals) code).getSkippableLines() - 1;
                        continue;
                    }
                }
                if (code instanceof AssignableIfLessThan) {
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
        PersonalityMatrix matrix = new PersonalityMatrix(getCode().length, pallet.length, palletsForInputs, palletReadOnly.length, false);
        for (int i = 0; i < getCode().length; i++) {
            matrix.getCode()[i] = getCode()[i] == null ? null : getCode()[i].clone(matrix);
        }
        return matrix;
    }

    public void randomizeSomeLines(double percetnage) {
        for (int i = 0; i < getCode().length; i++) {
            int chance = ThreadLocalRandom.current().nextInt((int) (100 / percetnage));
            if (chance == 0) {
                generateRandomNewAssignableCode(i, getCode().length);
            }
        }
    }
}
