package me.zombie_striker.swai.data;

import me.zombie_striker.swai.Main;
import me.zombie_striker.swai.assignablecode.*;
import me.zombie_striker.swai.assignablecode.statements.*;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PersonalityMatrix implements Comparable<PersonalityMatrix> {

    private static final int MAX_CODE_LINES_INCREASE_TO = 10000;

    private AssignableCode[] matrix;
    private int[] pallet;
    private int[] palletReadOnly;
    private UUID personalityUUID = UUID.randomUUID();
    private int palletsForInputs;

    private int fineTuningIndex;
    private int fineTuningVariableIndex;
    private int generation;

    private List<Integer> fieldsReadable = new LinkedList<>();
    private List<Integer> fieldsAll = new LinkedList<>();
    private List<Integer> fieldsWritable = new LinkedList<>();
    private List<Integer> arrays = new LinkedList<>();
    private List<Integer> markerIndexes = new LinkedList<>();
    private HashMap<Integer, Integer> preproccessedJumps = new HashMap<>();
    private boolean preproccessed = false;

    public int getIntAtField(int fieldid) {
        if (fieldid < 0 || getCode().length <= fieldid)
            return -1;
        if (!(getCode()[fieldid] instanceof AssignableField))
            return -1;
        return getIntAtField((AssignableField) getCode()[fieldid]);
    }

    public int getIntAtField(AssignableField field) {
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

    public PersonalityMatrix(int linesofposiblecode, int maxRam, int ramInputVariables, int readOnlyRamSize, boolean randomlyGenerateCode, int generation) {
        this.matrix = new AssignableCode[linesofposiblecode];
        this.pallet = new int[maxRam];
        this.palletsForInputs = ramInputVariables;
        this.palletReadOnly = new int[readOnlyRamSize];
        this.generation = generation;
        if (randomlyGenerateCode) {
            int startingline = generateFields();
            this.fineTuningIndex = (startingline * 25 * 9) - 1;
            this.fineTuningVariableIndex = 0;
            randomizeSomeLines(20);
            //randomizeCode(startingline);
        }
        populateRam();
    }

    public void setFineTuningIndex(int index) {
        this.fineTuningIndex = index;
    }

    public int getFineTuningIndex() {
        return fineTuningIndex;
    }

    public void populateRam() {
        populateRam(0);
    }

    public void populateRam(int numbersToFillTo) {
        for (int index = palletsForInputs; index <pallet.length; index++) {
            pallet[index] = index < numbersToFillTo ? index : 0;
        }
    }

    public void randomizeCode(int startingline) {
        int lines = DataBank.seededRandom(68, startingline, generation).nextInt(matrix.length);
        personalityUUID = UUID.randomUUID();
        for (int i = startingline; i < lines; i++) {
            generateRandomNewAssignableCode(i, lines);
        }
    }

    public int generateFields() {
        int fields = 0;
        for (int i = 0; i < getPalletReadOnly().length; i++) {
            String name;
            if (getPalletReadOnly().length >= DataBank.chars.length * DataBank.chars.length) {
                name = DataBank.chars[i / (DataBank.chars.length * DataBank.chars.length)] + "" + DataBank.chars[i / (DataBank.chars.length) % DataBank.chars.length] + "" + DataBank.chars[i % DataBank.chars.length];
            } else if (getPalletReadOnly().length >= DataBank.chars.length) {
                name = DataBank.chars[i / DataBank.chars.length] + "" + DataBank.chars[i % DataBank.chars.length];
            } else {
                name = "" + DataBank.chars[i];
            }
            matrix[fields] = new AssignableField(this, "F_" + name, i, true);
            fields++;
        }
        for (int i = 0; i < palletsForInputs; i++) {
            String name;

            if (pallet.length < DataBank.chars.length) {
                name = "" + DataBank.chars[i];
             } else if (pallet.length < DataBank.chars.length * DataBank.chars.length) {
                name = DataBank.chars[i / DataBank.chars.length] + "" + DataBank.chars[i % DataBank.chars.length];
            } else {
                name = DataBank.chars[i / (DataBank.chars.length * DataBank.chars.length)] + "" + DataBank.chars[i / (DataBank.chars.length) % DataBank.chars.length] + "" + DataBank.chars[i % DataBank.chars.length];
            }
            matrix[fields] = new AssignableField(this, "F_" + name, i, false);
            fields++;
        }
        return fields;
    }


    public void generateRandomNewAssignableCode(int index, int maxlines) {
        int random = DataBank.seededRandom(124, index, generation).nextInt(40);
        generateNewLine(index, random, maxlines);
    }

    private void generateNewLine(int index, int random, int maxlines) {

        if (getCode()[index] instanceof AssignableField && ((AssignableField) getCode()[index]).isReadOnly())
            return;
        if (getCode()[index] instanceof AssignableField && ((AssignableField) getCode()[index]).getReferenceID() < palletsForInputs)
            return;

        if (markerIndexes.contains(index)) {
            markerIndexes.remove((Object) index);
        }
        if (arrays.contains(index)) {
            arrays.remove((Object) index);
        }
        if (fieldsAll.contains(index)) {
            fieldsAll.remove((Object) index);
            fieldsWritable.remove((Object) index);
        }

        boolean insideArray = false;
        for(int i = index-1; i > 0 ; i--){
            if(matrix[i] instanceof AssignableCloseBracket){
                break;
            }
            if(matrix[i] instanceof AssignableArrayField){
                insideArray = true;
                break;
            }
        }

        if(insideArray){
            if(random % 3 == 0){
                int field = fieldsAll.get(DataBank.seededRandom(389,index,generation).nextInt(fieldsAll.size()));
                matrix[index] = new AssignableRef(field);
            }else if (random %3 == 1){
                matrix[index] = null;
            }else if(random % 3 == 2){
                matrix[index] = new AssignableCloseBracket();
            }
            return;
        }

        if (random == 0) {
            matrix[index] = new AssignableReturn();
        } else if (fieldsAll.size() < 2 && random == 1) {
            return;
        } else if (random == 1) {
            int field1 = fieldsAll.get(DataBank.seededRandom(130, index, generation).nextInt(fieldsAll.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(131, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableIfEquals(this, field1, field2);
        /*} else if (random == 2) {
            int skippablelines = DataBank.seededRandom(136, index, generation).nextInt(maxlines);

            matrix[index] = new AssignableJump(this, skippablelines, false);*/
            //TODO: ADD RANDOM OPTION 2
        } else if ((fieldsAll.size() < 1 || fieldsWritable.size() < 1) && random == 3) {
            return;
        } else if (random == 3) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(142, index, generation).nextInt(fieldsWritable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(143, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableSetField(this, field1, field2);
        } else if ((fieldsAll.size() < 1 || fieldsWritable.size() < 1) && random == 4) {
            return;
        } else if (random == 4) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(142, index, generation).nextInt(fieldsWritable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(143, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableSetRandField(this, index, field1, field2);
       /* } else if (fieldsAll.size() < 1 && random == 5) {
            return;
        } else if (random == 5) {
            int field1 = fieldsAll.get(DataBank.seededRandom(156, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableJump(this, field1, true);*/
            //TODO: ADD OPTION 5
        } else if (fieldsWritable.size() < 1 && random == 6) {
            return;
        } else if (random == 6) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(162, index, generation).nextInt(fieldsWritable.size()));

            matrix[index] = new AssignableIncrement(this, field1);

        } else if (random == 7) {

            matrix[index] = null;
        } else if (fieldsWritable.size() < 1 && random == 8) {
            return;
        } else if (random == 8) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(172, index, generation).nextInt(fieldsWritable.size()));

            matrix[index] = new AssignableDecrement(this, field1);
        } else if ((fieldsAll.size() < 2 || fieldsWritable.size() < 1) && random == 9) {
            return;
        } else if (random == 9) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(178, index, generation).nextInt(fieldsWritable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(179, index, generation).nextInt(fieldsAll.size()));
            int field3 = fieldsAll.get(DataBank.seededRandom(187, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableAdd(this, field1, field2, field3);
        } else if ((fieldsAll.size() < 2 || fieldsWritable.size() < 1) && random == 10) {
            return;
        } else if (random == 10) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(185, index, generation).nextInt(fieldsWritable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(186, index, generation).nextInt(fieldsAll.size()));
            int field3 = fieldsAll.get(DataBank.seededRandom(194, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableSubtract(this, field1, field2, field3);

        } else if (fieldsAll.size() < 2 && random == 11) {
            return;
        } else if (random == 11) {
            int field1 = fieldsAll.get(DataBank.seededRandom(193, index, generation).nextInt(fieldsAll.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(194, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableIfLessThan(this, field1, field2);

        } else if (random == 12) {
            int skippablelines = DataBank.seededRandom(200, index, generation).nextInt(maxlines);

            matrix[index] = new AssignableGoSub(skippablelines);
        } else if ((fieldsAll.size() < 1 || fieldsWritable.size() < 1) && random == 12) {
            return;
        } else if (random == 12) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(226, index, generation).nextInt(fieldsReadable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(226, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableSetSigmoid(this, field1, field2);
        } else if ((fieldsAll.size() < 2 || fieldsWritable.size() < 1) && random == 13) {
            return;
        } else if (random == 13) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(231, index, generation).nextInt(fieldsWritable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(232, index, generation).nextInt(fieldsAll.size()));
            int field3 = fieldsAll.get(DataBank.seededRandom(233, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableMultiplyBy(this, field1, field2, field3);
        } else if ((fieldsAll.size() < 2 || fieldsWritable.size() < 1) && random == 14) {
            return;
        } else if (random == 14) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(239, index, generation).nextInt(fieldsWritable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(240, index, generation).nextInt(fieldsAll.size()));
            int field3 = fieldsAll.get(DataBank.seededRandom(241, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableDivideBy(this, field1, field2, field3);
        } else if ((fieldsAll.size() < 2 || fieldsWritable.size() < 1) && random == 15) {
            return;
        } else if (random == 15) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(246, index, generation).nextInt(fieldsWritable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(247, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableModBy(this, field1, field2);
        } else if ((fieldsAll.size() < 3 || fieldsWritable.size() < 1) && random == 16) {
            return;
        } else if (random == 16) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(253, index, generation).nextInt(fieldsWritable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(254, index, generation).nextInt(fieldsAll.size()));
            int field3 = fieldsAll.get(DataBank.seededRandom(255, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableMax(this, field1, field2, field3);
        } else if ((fieldsAll.size() < 3 || fieldsWritable.size() < 1) && random == 17) {
            return;
        } else if (random == 17) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(261, index, generation).nextInt(fieldsWritable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(262, index, generation).nextInt(fieldsAll.size()));
            int field3 = fieldsAll.get(DataBank.seededRandom(263, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableMin(this, field1, field2, field3);
        } else if ((fieldsAll.size() < 2 || fieldsWritable.size() < 1) && random == 18) {
            return;
        } else if (random == 18) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(270, index, generation).nextInt(fieldsWritable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(271, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableAbsolute(this, field1, field2);
        } else if ((fieldsAll.size() < 4 || fieldsWritable.size() < 1) && random == 19) {
            return;
        } else if (random == 19) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(277, index, generation).nextInt(fieldsWritable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(278, index, generation).nextInt(fieldsAll.size()));
            int field3 = fieldsAll.get(DataBank.seededRandom(279, index, generation).nextInt(fieldsAll.size()));
            int field4 = fieldsAll.get(DataBank.seededRandom(280, index, generation).nextInt(fieldsAll.size()));
            int field5 = fieldsAll.get(DataBank.seededRandom(281, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableDistance(this, field1, field2, field3, field4, field5);
        } else if ((fieldsAll.size() < 2 || fieldsWritable.size() < 1) && random == 20) {
            return;
        } else if (random == 20) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(239, index, generation).nextInt(fieldsWritable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(240, index, generation).nextInt(fieldsAll.size()));
            int field3 = fieldsAll.get(DataBank.seededRandom(241, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableChangeBit(this, field1, field2, field3);

        } else if (fieldsAll.size() < 2 && random == 21) {
            return;
        } else if (random == 21) {
            int field1 = fieldsAll.get(DataBank.seededRandom(130, index, generation).nextInt(fieldsAll.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(131, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableIfBoolean(this, field1, field2);
        } else if (random == 22) {
            String name = "" + DataBank.chars[DataBank.seededRandom(287, getCode().length, generation).nextInt(DataBank.chars.length)] + DataBank.chars[DataBank.seededRandom(287, getCode().length, generation).nextInt(DataBank.chars.length)];
            matrix[index] = new AssignableMarker(name);
            markerIndexes.add(index);
        } else if (random == 23) {
            if (markerIndexes.size() == 0)
                return;
            String name = ((AssignableMarker) this.getCode()[(markerIndexes.get(DataBank.seededRandom(getGeneration(), 294, markerIndexes.size()).nextInt(markerIndexes.size())))]).getMarkerName();
            matrix[index] = new AssignableSeek(name);
        } else if (random == 24) {
            int freeSpot = -1;
            indexsearch:
            for (int i = 0; i < pallet.length; i++) {
                for (Integer f : fieldsWritable) {
                    if (f < getCode().length)
                        if (((AssignableField) getCode()[f]).getReferenceID() == i) {
                            continue indexsearch;
                        }
                }
                freeSpot = i;
                break;
            }
            if (freeSpot == -1)
                return;
            String name;
            if (freeSpot >= DataBank.chars.length * DataBank.chars.length * DataBank.chars.length) {
                name = DataBank.chars[freeSpot / (DataBank.chars.length * DataBank.chars.length * DataBank.chars.length)] + "" + DataBank.chars[(freeSpot / (DataBank.chars.length * DataBank.chars.length)) % DataBank.chars.length] + "" + DataBank.chars[(freeSpot / DataBank.chars.length) % DataBank.chars.length] + "" + DataBank.chars[freeSpot % DataBank.chars.length];
            } else if (freeSpot >= DataBank.chars.length * DataBank.chars.length) {
                name = DataBank.chars[freeSpot / (DataBank.chars.length * DataBank.chars.length)] + "" + DataBank.chars[(freeSpot / DataBank.chars.length) % DataBank.chars.length] + "" + DataBank.chars[freeSpot % DataBank.chars.length];
            } else if (freeSpot >= DataBank.chars.length) {
                name = DataBank.chars[freeSpot / DataBank.chars.length] + "" + DataBank.chars[freeSpot % DataBank.chars.length];
            } else {
                name = "" + DataBank.chars[freeSpot];
            }
            matrix[index] = new AssignableField(this, "F_" + name, freeSpot, false);
            fieldsAll.add(index);
            fieldsWritable.add(index);
        } else if (random == 25) {
            matrix[index] = new AssignableJumpOver(this);
        } else if ((fieldsAll.size() < 1) && random == 26) {
            return;
        } else if (random == 26) {
            int size = DataBank.seededRandom(349, generation, index).nextInt(fieldsAll.size());
            String name;
            if (arrays.size() >= DataBank.chars.length * DataBank.chars.length * DataBank.chars.length) {
                name = DataBank.chars[arrays.size() / (DataBank.chars.length * DataBank.chars.length * DataBank.chars.length)] + "" + DataBank.chars[(arrays.size() / (DataBank.chars.length * DataBank.chars.length)) % DataBank.chars.length] + "" + DataBank.chars[(arrays.size() / DataBank.chars.length) % DataBank.chars.length] + "" + DataBank.chars[arrays.size() % DataBank.chars.length];
            } else if (arrays.size() >= DataBank.chars.length * DataBank.chars.length) {
                name = DataBank.chars[arrays.size() / (DataBank.chars.length * DataBank.chars.length)] + "" + DataBank.chars[(arrays.size() / DataBank.chars.length) % DataBank.chars.length] + "" + DataBank.chars[arrays.size() % DataBank.chars.length];
            } else if (arrays.size() >= DataBank.chars.length) {
                name = DataBank.chars[arrays.size() / DataBank.chars.length] + "" + DataBank.chars[arrays.size() % DataBank.chars.length];
            } else {
                name = "" + DataBank.chars[arrays.size()];
            }
            matrix[index] = new AssignableArrayField(this, "A_" + name, index);
            arrays.add(index);

        } else if ((fieldsWritable.size() < 1 || arrays.size() < 1) && random == 27) {
            return;
        } else if (random == 27) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(368, index, generation).nextInt(fieldsWritable.size()));
            int array = arrays.get(DataBank.seededRandom(369, index, generation).nextInt(arrays.size()));

            matrix[index] = new AssignableNeuron(this, field1, array);
        } else if ((fieldsAll.size() < 2 || fieldsWritable.size() < 1) && random == 28) {
            return;
        } else if (random == 28) {
            int field1 = fieldsWritable.get(DataBank.seededRandom(377, index, generation).nextInt(fieldsWritable.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(378, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableSin(this, field1, field2);
        } else if ((fieldsAll.size() < 1 || arrays.size() < 1) && random == 29) {
            return;
        } else if (random == 29) {
            int field1 = arrays.get(DataBank.seededRandom(384, index, generation).nextInt(arrays.size()));
            int field2 = fieldsAll.get(DataBank.seededRandom(385, index, generation).nextInt(fieldsAll.size()));

            matrix[index] = new AssignableIfEquals(this, field1, field2);
        } else if (random >= 30 && random <= 35) {
            matrix[index] = new AssignableCloseBracket();
        }else if (random == 36) {
            int field = fieldsAll.get(DataBank.seededRandom(389, index, generation).nextInt(fieldsAll.size()));
            matrix[index] = new AssignableRef(field);
        }else if (random == 37 && (fieldsWritable.size() < 1|| arrays.size() < 1)){
            return;
        }else if (random == 37){
            int writeToField = fieldsWritable.get(DataBank.seededRandom(416,index,generation).nextInt(fieldsWritable.size()));
            int arrayfield = arrays.get(DataBank.seededRandom(417,index,generation).nextInt(arrays.size()));
            matrix[index] = new AssignableForEach(this,arrayfield,writeToField);
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

        if (!preproccessed) {
            preproccessJumps();
        }

        linesRan = runCodeFrom(0, debug, maxDebugEntries,0);
        return linesRan;
    }

    private int runCodeFrom(int start, boolean debug, int maxDebugEntries, int functionIndex) {
        int debugentries = 0;
        int linesRan = 0;
        int stackIndex = 0;
        int[] stack = new int[10];
        int tabcount = functionIndex > 1?1:-1;
        for (int i = start; i < matrix.length; i++) {
            if (linesRan >= 10000) {
                //System.out.println(DataBank.ANSI_RED + "[Halting Program as it went over the 10k lines limit]" + DataBank.ANSI_RESET);
                break;
            }
            linesRan++;
            if (i < 0) {
                i = 0;
                continue;
            }
            AssignableCode code = matrix[i];
            if (code != null) {
                if (debug) {
                    if (debugentries < maxDebugEntries) {
                        System.out.println(i + " " + code.toString());
                        debugentries++;
                    }
                }
                if(functionIndex > 1 && code instanceof AssignableOpenBracketType)
                        tabcount++;

                if(functionIndex > 1 && code instanceof AssignableCloseBracket){
                    tabcount--;
                    if(tabcount <= 0)
                    return linesRan;
                }else if (code instanceof AssignableStatement) {
                    ((AssignableStatement) code).call();
               // } else if (code instanceof AssignableGoSub) {
                //    if (stackIndex >= stack.length) {
                 //       break;
                 //   }
                 //   stack[stackIndex] = i;
                 //   stackIndex++;
                 //   i = ((AssignableGoSub) code).getLineToJumpTo() - 1;
                 //   continue;
                } else if (code instanceof AssignableReturn) {
                    if (stackIndex > 0) {
                        stackIndex--;
                        i = stack[stackIndex];
                        continue;
                    } else {
                        break;
                    }
                } else if (code instanceof AssignableSeek) {
                    int firstCode = -1;
                    for (Integer ii : markerIndexes) {
                        if (getCode()[ii] instanceof AssignableMarker) {
                            if (((AssignableMarker) getCode()[ii]).getMarkerName().equals(((AssignableSeek) code).getMarkerName())) {
                                firstCode = ii;
                                break;
                            }
                        }
                    }
                    if (firstCode != -1)
                        i = firstCode;
                    continue;
              //  } else if (code instanceof AssignableJump) {
              //      i = ((AssignableJump) code).getLineToSkipTo() - 1;
              //      continue;
                }else if (code instanceof AssignableFor){
                    if(functionIndex < 10)
                    while(((AssignableFor) code).loopagain()){
                        linesRan += runCodeFrom(i+1,debug,maxDebugEntries, functionIndex+1);
                        if(linesRan > 10000)
                            break;
                    }
                } else if (code instanceof AssignableIf) {
                    if (!((AssignableIf) code).compare()) {
                        i = preproccessedJumps.get(i);
                        continue;
                    }
                } else if (code instanceof AssignableArrayField) {
                    i = preproccessedJumps.get(i);
                } else if (code instanceof AssignableJumpOver) {
                    i = preproccessedJumps.get(i);
                }
            }
        }
        return linesRan;
    }

    private void preproccessJumps() {
        for (int i = 0; i < getCode().length; i++) {
            if (getCode()[i] instanceof AssignableOpenBracketType) {
                int endBracketsRequired = 1;
                for (int j = i+1; j <= getCode().length; j++) {
                    if (j == getCode().length) {
                        preproccessedJumps.put(i, j);
                    } else if (getCode()[j] instanceof AssignableCloseBracket) {
                        endBracketsRequired--;
                    } else if (getCode()[j] instanceof AssignableOpenBracketType) {
                        endBracketsRequired++;
                    }
                    if (endBracketsRequired <= 0) {
                        preproccessedJumps.put(i, j);
                        break;
                    }
                }
            }
        }
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
        PersonalityMatrix matrix = new PersonalityMatrix(getCode().length, pallet.length, palletsForInputs, palletReadOnly.length, false, generation);
        for (int i = 0; i < getCode().length; i++) {
            matrix.getCode()[i] = getCode()[i] == null ? null : getCode()[i].clone(matrix);
        }
        matrix.populateRam();
        matrix.personalityUUID = personalityUUID;
        matrix.fineTuningIndex = fineTuningIndex;
        matrix.fineTuningVariableIndex = fineTuningVariableIndex;
        return matrix;
    }

    public void randomizeSomeLines(double rarity) {
        personalityUUID = UUID.randomUUID();

        if (fieldsAll.size() <= 0) {
            if (fieldsReadable.size() == 0 && fieldsWritable.size() == 0) {
                for (int i = 0; i < getCode().length; i++) {
                    AssignableCode code = getCode()[i];
                    if (code != null && code instanceof AssignableField) {
                        fieldsAll.add(i);
                        if (!((AssignableField) code).isReadOnly()) {
                            fieldsWritable.add(i);
                        } else {
                            fieldsReadable.add(i);
                        }
                    }
                }
            }
        }


        int randomFunctionEvent = DataBank.seededRandom(generation, getCode().length, 7).nextInt(100);
        if (randomFunctionEvent <= 10) {
            //shrink
            if (getCode().length - 50 > pallet.length + palletReadOnly.length)
                shrinkCodeBase(50);
        } else if (randomFunctionEvent <= 20) {
            //increaseToMax
            if (getCode().length < MAX_CODE_LINES_INCREASE_TO)
                increaseCodeBase(50);
        } else if (randomFunctionEvent <= 30) {
            //cullUnusedLines();
        }


        for (int i = 0; i < getCode().length; i++) {
            int whatDo = DataBank.seededRandom(i + 27, getCode().length, generation).nextInt(20);
            int chance = DataBank.seededRandom(generation, i + 5, getCode().length).nextInt((int) Math.max(1, Main.MAX_PERSONALITIES_PER_GAME - rarity));

            if (whatDo <= 8) {
                tweakSomeVariables(i, whatDo, getCode().length);
            } else if (chance == 0) {
                generateRandomNewAssignableCode(i, getCode().length);
            }
        }
    }

    public void tweakSomeVariables(int line, int variableIndex, int maxlines) {
        if (true)
            return;
        //TODO: Figure out how to safely tweak variables
        AssignableCode code = getCode()[line];
        int tweakDirection = DataBank.seededRandom(line * variableIndex, maxlines, 411).nextInt(10) - 5;
        if (tweakDirection >= 0) {
            tweakDirection++;
        }

        if (code != null) {
            /*if (code instanceof AssignableJump) {
                if (variableIndex == 0) {
                    if (((AssignableJump) code).usesField()) {
                        ((AssignableJump) code).setSkippableLines(((AssignableJump) code).getLineToSkipTo() + tweakDirection);
                    } else if (variableIndex == 1) {
                        ((AssignableJump) code).setSkippableLines(((AssignableJump) code).getLineToSkipTo() + tweakDirection);
                    }
                }
            } else */if (code instanceof AssignableGoSub) {
                if (variableIndex == 0) {
                    ((AssignableGoSub) code).setSkippableLines(((AssignableGoSub) code).getLineToJumpTo() + tweakDirection);
                }
            } else if (code instanceof AssignableIfLessThan) {
                if (variableIndex == 0) {
                    ((AssignableIfLessThan) code).setVar1(((AssignableIfLessThan) code).getVar1() + tweakDirection);
                } else if (variableIndex == 1) {
                    ((AssignableIfLessThan) code).setVar2(((AssignableIfLessThan) code).getVar2() + tweakDirection);
                }
            } else if (code instanceof AssignableIfEquals) {
                if (variableIndex == 0) {
                    ((AssignableIfEquals) code).setVar1(((AssignableIfEquals) code).getVar1() + tweakDirection);
                } else if (variableIndex == 1) {
                    ((AssignableIfEquals) code).setVar2(((AssignableIfEquals) code).getVar2() + tweakDirection);
                }

            } else if (code instanceof AssignableSetField) {
                if (variableIndex == 0) {
                    ((AssignableSetField) code).setFieldindex(((AssignableSetField) code).getField() + tweakDirection);
                } else if (variableIndex == 1) {
                    ((AssignableSetField) code).setVar2(((AssignableSetField) code).getSecondField() + tweakDirection);
                }


            }
        }
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    @Override
    public int compareTo(PersonalityMatrix o) {
        return getGeneration() - o.getGeneration();
    }

    public static PersonalityMatrix load(File savefile) {
        try {
            String data = DataBank.readFile(new FileInputStream(savefile));
            String[] text = data.split("\n");
            String[] codelines = new String[text.length - 4];

            int ram = Integer.parseInt(text[0]);
            int input = Integer.parseInt(text[1]);
            int readonly = Integer.parseInt(text[2]);
            int gen = Integer.parseInt(text[3]);

            for (int i = 0; i < codelines.length; i++) {
                codelines[i] = text[i + 4];
            }
            return load(codelines, ram, input, readonly, gen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void increaseCodeBase(int byHowMuch) {
        AssignableCode[] code = new AssignableCode[getCode().length + byHowMuch];
        for (int i = 0; i < getCode().length; i++) {
            code[i] = getCode()[i];
        }
        this.matrix = code;
    }

    public void shrinkCodeBase(int byHowMuch) {
        AssignableCode[] code = new AssignableCode[getCode().length - byHowMuch];
        for (int i = 0; i < code.length; i++) {
            code[i] = getCode()[i];
        }
        this.matrix = code;
    }

    public static PersonalityMatrix load(String[] lines, int ram, int input, int readonly, int gen) {
        PersonalityMatrix matrix = new PersonalityMatrix(lines.length, ram, input, readonly, false, gen);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].toUpperCase().startsWith("RETURN")) {
                matrix.getCode()[i] = new AssignableReturn();
            } else if (lines[i].toUpperCase().startsWith("JUMPOVER")) {
                matrix.getCode()[i] = new AssignableJumpOver(matrix);
            } else if (lines[i].toUpperCase().startsWith("}")) {
                matrix.getCode()[i] = new AssignableCloseBracket();
            } else if (lines[i].toUpperCase().startsWith("REF")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int d = -1;
                try {
                    d = Integer.parseInt(data[0]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableRef(d);
           /* } else if (lines[i].toUpperCase().startsWith("JUMPTO")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int jumpto = -1;
                try {
                    jumpto = Integer.parseInt(data[0]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableJump(matrix, jumpto, false);
            } else if (lines[i].toUpperCase().startsWith("JUMP")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int jumpto = -1;
                try {
                    jumpto = Integer.parseInt(data[0]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableJump(matrix, jumpto, true);*/
            } else if (lines[i].toUpperCase().startsWith("FOREACH")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int array = -1;
                int field = -1;
                try {
                    array = Integer.parseInt(data[0]);
                    field = Integer.parseInt(data[1]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableForEach(matrix,array,field);
            } else if (lines[i].toUpperCase().startsWith("IFLESSTHAN")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableIfLessThan(matrix, f1, f2);
            } else if (lines[i].toUpperCase().startsWith("MARKER")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                String name = data[0];
                matrix.getCode()[i] = new AssignableMarker(name);
            } else if (lines[i].toUpperCase().startsWith("SEEK")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                String name = data[0];
                matrix.getCode()[i] = new AssignableSeek(name);
            } else if (lines[i].toUpperCase().startsWith("IFEQUALS")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableIfEquals(matrix, f1, f2);
            } else if (lines[i].toUpperCase().startsWith("IFBOOL")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableIfBoolean(matrix, f1, f2);
            } else if (lines[i].toUpperCase().startsWith("GOSUB")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int jumpto = -1;
                try {
                    jumpto = Integer.parseInt(data[0]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableGoSub(jumpto);
            } else if (lines[i].toUpperCase().startsWith("RAND")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                int magicnumber = i;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableSetRandField(matrix, magicnumber, f1, f2);
            } else if (lines[i].toUpperCase().startsWith("FIELDRO")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                String name = null;
                try {
                    name = data[0];
                    f1 = Integer.parseInt(data[1]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableField(matrix, name, f1, true);
            } else if (lines[i].toUpperCase().startsWith("FIELD")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                String name = null;
                try {
                    name = data[0];
                    f1 = Integer.parseInt(data[1]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableField(matrix, name, f1, false);
            } else if (lines[i].toUpperCase().startsWith("MIN")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                int f3 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                    f3 = Integer.parseInt(data[2]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableMin(matrix, f1, f2, f3);
            } else if (lines[i].toUpperCase().startsWith("MAX")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                int f3 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                    f3 = Integer.parseInt(data[2]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableMax(matrix, f1, f2, f3);
            } else if (lines[i].toUpperCase().startsWith("DISTANCE")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                int f3 = -1;
                int f4 = -1;
                int f5 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                    f3 = Integer.parseInt(data[2]);
                    f4 = Integer.parseInt(data[3]);
                    f5 = Integer.parseInt(data[4]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableDistance(matrix, f1, f2, f3, f4, f5);
            } else if (lines[i].toUpperCase().startsWith("MULTIPLY")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                int f3 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                    f3 = Integer.parseInt(data[2]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableMultiplyBy(matrix, f1, f2, f3);
            } else if (lines[i].toUpperCase().startsWith("DIVIDE")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                int f3 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                    f3 = Integer.parseInt(data[2]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableDivideBy(matrix, f1, f2, f3);
            } else if (lines[i].toUpperCase().startsWith("MOD")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableModBy(matrix, f1, f2);
            } else if (lines[i].toUpperCase().startsWith("SIGMOID")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[0]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableSetSigmoid(matrix, f1, f2);
            } else if (lines[i].toUpperCase().startsWith("SET")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableSetField(matrix, f1, f2);
            } else if (lines[i].toUpperCase().startsWith("SIN")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableSin(matrix, f1, f2);
            } else if (lines[i].toUpperCase().startsWith("ABS")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableAbsolute(matrix, f1, f2);
            } else if (lines[i].toUpperCase().startsWith("ADD")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                int f3 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                    f3 = Integer.parseInt(data[2]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableAdd(matrix, f1, f2, f3);
            } else if (lines[i].toUpperCase().startsWith("SUB")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                int f3 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                    f3 = Integer.parseInt(data[2]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableSubtract(matrix, f1, f2, f3);
            } else if (lines[i].toUpperCase().startsWith("DEC")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableDecrement(matrix, f1);
            } else if (lines[i].toUpperCase().startsWith("INC")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableIncrement(matrix, f1);
            } else if (lines[i].toUpperCase().startsWith("ARRAY")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                String name = null;
                try {
                    name = data[0];
                } catch (Exception e4) {

                }

                matrix.getCode()[i] = new AssignableArrayField(matrix, name, i);
            } else if (lines[i].toUpperCase().startsWith("NEURON")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int a2 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    a2 = Integer.parseInt(data[1]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableNeuron(matrix, f1, a2);
            } else if (lines[i].toUpperCase().startsWith("CHANGEBIT")) {
                String[] data = lines[i].split("\\(")[1].split("\\)")[0].split(",");
                int f1 = -1;
                int f2 = -1;
                int f3 = -1;
                try {
                    f1 = Integer.parseInt(data[0]);
                    f2 = Integer.parseInt(data[1]);
                    f3 = Integer.parseInt(data[2]);
                } catch (Exception e3) {
                }
                matrix.getCode()[i] = new AssignableChangeBit(matrix, f1, f2, f3);
            } else if (lines[i] == null || lines[i].equals("") || lines[i].equalsIgnoreCase("NULL")) {
            } else {
                System.out.println("FAILED TO READ : " + lines[i]);
            }
        }
        return matrix;
    }

    public void saveTo(File writeTo) {
        try {
            FileWriter fw = new FileWriter(writeTo);
            fw.write(pallet.length + "\n");
            fw.write(palletsForInputs + "\n");
            fw.write(palletReadOnly.length + "\n");
            fw.write(generation + "\n");
            for (int line2 = 0; line2 < getCode().length; line2++) {
                if (getCode()[line2] == null) {
                    fw.write("");
                } else {
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

    /*public void cullUnusedLines() {
        boolean[] usedlines = new boolean[getCode().length];
        int linesran = 0;
        for (int i = 0; i < getCode().length; i++) {
            AssignableCode code = getCode()[i];
            if (code != null) {
                usedlines[i] = true;
                int[] k = branchForCull(usedlines, code, i, linesran);
                int j = k[0];
                int l = k[1];
                if (j == -1)
                    break;
                i = j - 1;
                linesran += l;
                if (linesran >= 10000)
                    break;
            }
        }
        for (int i = 0; i < usedlines.length; i++) {
            if (!usedlines[i])
                getCode()[i] = null;
        }
    }*/
    /*
    public int[] branchForCull(boolean[] b, AssignableCode code, int lineindex, int linesRan) {
        if (lineindex >= 10000)
            return new int[]{-1, 0};
        if (code instanceof AssignableField) {
            b[lineindex] = true;
            return new int[]{lineindex + 1, 1};
        }
        if (code instanceof AssignableReturn) {
            b[lineindex] = true;
            return new int[]{-1, 1};
        }
        if (code instanceof AssignableStatement) {
            b[lineindex] = true;
            return new int[]{lineindex + 1, 1};
        }
        if (code instanceof AssignableJump) {
            if (((AssignableJump) code).usesField()) {
                //TODO: Figure out possible values for each field
            } else {
                b[lineindex] = true;
                return new int[]{((AssignableJump) code).getLineToSkipTo(), 1};
            }
        }
        if (code instanceof AssignableIfEquals) {
            b[lineindex] = true;
            for (int j = 0; j < ((AssignableIfEquals) code).getSkippableLines(); j++) {
                int[] temp = branchForCull(b, getCode()[lineindex + j + 1], lineindex + j + 1, linesRan);
                int linesRanNow = temp[1];
                if (temp[0] == -1) {
                    return new int[]{lineindex + ((AssignableIfEquals) code).getSkippableLines(), linesRanNow};
                }
            }
            return new int[]{lineindex + ((AssignableIfEquals) code).getSkippableLines()};
        }
        if (code instanceof AssignableIfBoolean) {
            b[lineindex] = true;
            for (int j = 0; j < ((AssignableIfBoolean) code).getSkippableLines(); j++) {
                int[] temp = branchForCull(b, getCode()[lineindex + j + 1], lineindex + j + 1, linesRan);
                int linesRanNow = temp[1];
                if (temp[0] == -1) {
                    return new int[]{lineindex + ((AssignableIfBoolean) code).getSkippableLines(), linesRanNow};
                }
            }
            return new int[]{lineindex + ((AssignableIfBoolean) code).getSkippableLines()};
        }
        if (code instanceof AssignableIfLessThan) {
            b[lineindex] = true;
            for (int j = 0; j < ((AssignableIfLessThan) code).getSkippableLines(); j++) {
                int[] temp = branchForCull(b, getCode()[lineindex + j + 1], lineindex + j + 1, linesRan);
                int linesRanNow = temp[1];
                if (temp[0] == -1) {
                    return new int[]{lineindex + ((AssignableIfLessThan) code).getSkippableLines(), linesRanNow};
                }
            }
            return new int[]{lineindex + ((AssignableIfLessThan) code).getSkippableLines()};
        }
        if (code instanceof AssignableGoSub) {
            int totalLines = 0;
            b[lineindex] = true;
            for (int j = 0; j < 10000 - linesRan; j++) {
                int[] temp = branchForCull(b, getCode()[((AssignableGoSub) code).getLineToJumpTo()], ((AssignableGoSub) code).getLineToJumpTo(), linesRan);
                int linesRanNow = temp[1];
                totalLines += linesRanNow;
                if (temp[0] == -1) {
                    return new int[]{lineindex + 1, linesRanNow};
                }
                if (linesRan + linesRanNow + j > 10000)
                    break;
            }
            return new int[]{lineindex + 1, totalLines};
        }
        return new int[]{lineindex + 1, 1};
    }*/


    public void improveCode(double v) {
        if (Main.gameworldInterpreter.hasEnabledFineTuning()) {
            int i = fineTuningIndex / (30 * 9);
            generateNewLine(i, fineTuningIndex % 30, getCode().length);
            //Reset it back to above the field for recalibration
            fineTuningIndex = ((pallet.length + palletReadOnly.length) * 30 * 9) - 1;
            fineTuningVariableIndex = 0;
        } else {
            randomizeSomeLines(v);
        }
    }
}
