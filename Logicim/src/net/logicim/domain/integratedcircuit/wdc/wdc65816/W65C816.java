package net.logicim.domain.integratedcircuit.wdc.wdc65816;

import net.common.SimulatorException;
import net.common.util.IntUtil;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.wire.TraceValue;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.BusCycle;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.Instruction;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.InstructionFactory;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address.InstructionCycles;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations.DataOperation;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations.Operation;

import static net.common.util.IntUtil.*;
import static net.common.util.StringUtil.*;
import static net.logicim.domain.integratedcircuit.wdc.wdc65816.CpuFlags.*;

public class W65C816
    extends IntegratedCircuit<W65C816Pins, W65C816State>
{
  public static final String TYPE = "W65C816 Microprocessor";

  protected static Instruction[] opCodeTable;
  protected static Instruction resetOpcode;
  protected static Instruction abortOpcode;
  protected static Instruction irqOpcode;
  protected static Instruction nmiOpcode;
  protected static Instruction fetchNextOpcode;

  // Status register
  protected boolean zeroFlag;
  protected boolean negativeFlag;
  protected boolean decimalFlag;
  protected boolean interruptDisableFlag;
  protected boolean accumulatorWidthFlag;
  protected boolean indexWidthFlag;
  protected boolean carryFlag;
  protected boolean emulationFlag;
  protected boolean overflowFlag;
  protected boolean breakFlag;

  // Registers
  protected int accumulator;
  protected int xIndex;
  protected int yIndex;
  protected int dataBank;
  protected int directPage;
  protected Address programCounter;
  protected int stackPointer;

  protected int cycle;
  protected Instruction opCode;
  protected boolean stopped;

  protected int abortProcessRegister;
  protected int abortAccumulator;
  protected int abortXIndex;
  protected int abortYIndex;
  protected int abortDataBank;
  protected int abortDirectPage;
  protected Address abortProgramCounter;
  protected int abortStackPointer;

  //These are not the values on the pins, they are internal data.
  protected Address address;
  protected int internal16BitData;
  protected int directOffset;
  protected Address newProgramCounter;
  protected boolean busEnable;
  protected boolean irq;
  protected boolean abort;
  protected boolean nextInstruction;
  protected int data;
  protected boolean nmi;

  protected String name;

  public W65C816(Circuit circuit, String name, W65C816Pins pins)
  {
    super(circuit, name, pins);

    programCounter = new Address(0x00, 0x0000);
    stackPointer = 0x01FF;
    opCodeTable = InstructionFactory.createInstructions();
    resetOpcode = InstructionFactory.createReset();
    irqOpcode = InstructionFactory.createIRQ();
    nmiOpcode = InstructionFactory.createNMI();
    abortOpcode = InstructionFactory.createAbort();
    fetchNextOpcode = InstructionFactory.createFetchNext();
    accumulator = 0;
    xIndex = 0;
    yIndex = 0;
    dataBank = 0;
    directPage = 0;

    zeroFlag = false;
    negativeFlag = false;
    decimalFlag = false;
    interruptDisableFlag = false;
    accumulatorWidthFlag = false;
    indexWidthFlag = false;
    carryFlag = false;
    emulationFlag = true;
    overflowFlag = false;
    breakFlag = false;

    stopped = false;
    busEnable = true;
    nextInstruction = false;

    data = 0;
    cycle = 0;
    opCode = resetOpcode;
    internal16BitData = 0;
    directOffset = 0;
    newProgramCounter = new Address();
    address = new Address();
    nmi = false;

    createAbortValues();
  }

  private void createAbortValues()
  {
    abortProcessRegister = getProcessorRegisterValue();
    abortAccumulator = accumulator;
    abortXIndex = xIndex;
    abortYIndex = yIndex;
    abortDataBank = dataBank;
    abortDirectPage = directPage;
    abortProgramCounter = new Address(programCounter);
    abortStackPointer = stackPointer;
  }

  public void createPartialAbortValues()
  {
    abortProcessRegister = getProcessorRegisterValue();
    abortDataBank = dataBank;
    abortProgramCounter = new Address(programCounter.getBank(), abortProgramCounter.getOffset());
  }

  public void restoreAbortValues()
  {
    abortProcessRegister = getProcessorRegisterValue();
    accumulator = abortAccumulator;
    xIndex = abortXIndex;
    yIndex = abortYIndex;
    dataBank = abortDataBank;
    directPage = abortDirectPage;
    programCounter = new Address(abortProgramCounter);
    stackPointer = abortStackPointer;
  }

  public void setX(int xIndex)
  {
    if (isIndex16Bit())
    {
      assert16Bit(xIndex, "X Index register");
    }
    else
    {
      assert8Bit(xIndex, "X Index register");
    }
    this.xIndex = xIndex;
    setSignAndZeroFromIndex(xIndex);
  }

  public void setY(int yIndex)
  {
    if (isIndex16Bit())
    {
      assert16Bit(yIndex, "Y Index register");
    }
    else
    {
      assert8Bit(yIndex, "Y Index register");
    }
    this.yIndex = yIndex;
    setSignAndZeroFromIndex(yIndex);
  }

  public void setA(int accumulator)
  {
    if (isMemory16Bit())
    {
      assert16Bit(accumulator, "Accumulator");
      this.accumulator = accumulator;
    }
    else
    {
      assert8Bit(accumulator, "Accumulator");
      this.accumulator = setLowByte(this.accumulator, accumulator);
    }
    setSignAndZeroFromMemory(accumulator);
  }

  public void setC(int accumulator)
  {
    assert16Bit(accumulator, "Accumulator");
    this.accumulator = accumulator;
    setSignAndZeroFlagFrom16BitValue(accumulator);
  }

  public void setData(int data, boolean updateFlags)
  {
    if (isMemory16Bit())
    {
      assert16Bit(data, "Data");
      this.internal16BitData = data;
    }
    else
    {
      assert8Bit(data, "Data");
      this.internal16BitData = setLowByte(this.internal16BitData, data);
    }
    if (updateFlags)
    {
      setSignAndZeroFromMemory(data);
    }
  }

  public void setIndexData(int data, boolean updateFlags)
  {
    if (isIndex16Bit())
    {
      assert16Bit(data, "Data");
      this.internal16BitData = data;
    }
    else
    {
      assert8Bit(data, "Data");
      this.internal16BitData = setLowByte(this.internal16BitData, data);
    }
    if (updateFlags)
    {
      setSignAndZeroFromIndex(data);
    }
  }

  private void setSignAndZeroFromMemory(int value)
  {
    if (isMemory16Bit())
    {
      setSignAndZeroFlagFrom16BitValue(value);
    }
    else
    {
      setSignAndZeroFlagFrom8BitValue(value);
    }
  }

  private void setSignAndZeroFromIndex(int value)
  {
    if (isIndex16Bit())
    {
      setSignAndZeroFlagFrom16BitValue(value);
    }
    else
    {
      setSignAndZeroFlagFrom8BitValue(value);
    }
  }

  public void setInternal16BitData(int internal16BitData)
  {
    setData(internal16BitData, true);
  }

  public int getA()
  {
    if (isMemory16Bit())
    {
      return accumulator;
    }
    else
    {
      return toByte(accumulator);
    }
  }

  public int getC()
  {
    return accumulator;
  }

  public int getX()
  {
    if (isIndex16Bit())
    {
      return xIndex;
    }
    else
    {
      return toByte(xIndex);
    }
  }

  public int getY()
  {
    if (isIndex16Bit())
    {
      return yIndex;
    }
    else
    {
      return toByte(yIndex);
    }
  }

  public int getDataBank()
  {
    return dataBank;
  }

  public void setDataBank(int dataBank)
  {
    assert8Bit(dataBank, "Data Bank");
    this.dataBank = dataBank;
  }

  public void setDirectPage(int directPage)
  {
    assert16Bit(directPage, "Direct Page");
    this.directPage = directPage;
  }

  public Address getProgramCounter()
  {
    return programCounter;
  }

  public W65C816Pins getPins()
  {
    return pins;
  }

  public void reset()
  {
    super.reset();
    resetPulled();
  }

  public void resetPulled()
  {
    abort = false;
    nmi = false;
    opCode = resetOpcode;
    stopped = false;
    cycle = 0;
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {

  }

  @Override
  public void inputTransition(Simulation simulation, LogicPort port)
  {
    long simulationTime = simulation.getTime();

    LogicPort res = getPins().getResB();
    if (port == res)
    {
      boolean reset = !res.readValue(simulationTime).isLow();
      if (reset)
      {
        resetPulled();
      }
    }

    LogicPort clock = getPins().getPhi2();
    LogicPort nmi = getPins().getNmiB();
    LogicPort irq = getPins().getIrqB();
    LogicPort abort = getPins().getAbortB();
    LogicPort be = getPins().getBe();

    if (clock == port)
    {
      TraceValue clockValue = clock.readValue(simulationTime);
      if (clockValue.isHigh() || clockValue.isLow())
      {
        boolean currentClock = clockValue.isHigh();
        boolean fallingEdge = !currentClock;

        if (!busEnable)
        {
          disableBuses();
        }

        if (!stopped)
        {
          if (fallingEdge)
          {
            executeLowHalf(simulation.getTimeline());
          }

          if (currentClock)
          {
            executeHighHalf(simulation.getTimeline());
          }
        }
      }
    }
    else if (port == nmi)
    {
      this.nmi = !nmi.readValue(simulationTime).isLow();
    }
    else if (port == irq)
    {
      this.irq = !irq.readValue(simulationTime).isLow();
    }
    else if (port == abort)
    {
      this.abort = !abort.readValue(simulationTime).isLow();
    }
    else if (port == be)
    {
      this.busEnable = be.readValue(simulationTime).isLow();
    }
  }

  private void disableBuses()
  {

  }

  public final void executeLowHalf(Timeline timeline)
  {
    DataOperation dataOperation = getDataOperation();
    BusCycle busCycle = getBusCycle();
    boolean read = dataOperation.isRead();
    Address address = busCycle.getAddress(this);

    pins.getRwb().writeBool(timeline, read);
    pins.getMx().writeBool(timeline, isIndex8Bit());
    pins.getVda().writeBool(timeline, dataOperation.isValidDataAddress());
    pins.getVpa().writeBool(timeline, dataOperation.isValidProgramAddress());
    pins.getMlB().writeBool(timeline, dataOperation.isNotMemoryLock());
    pins.getVpB().writeBool(timeline, dataOperation.isNotVectorPull());
    pins.getE().writeBool(timeline, isEmulation());
    //pins.getRdy().writeBool(timeline, dataOperation.isReady());
    pins.writeAddress(timeline, address.getOffset());
    pins.writeData(timeline, address.getBank());
  }

  public final void executeHighHalf(Timeline timeline)
  {
    DataOperation dataOperation = getDataOperation();
    BusCycle busCycle = getBusCycle();
    boolean read = dataOperation.isRead();
    Address address = busCycle.getAddress(this);

    if (read)
    {
      data = pins.readData(timeline);
    }

//    pins.getRwb().writeBool(timeline, read);
//    pins.getMlB().writeBool(timeline, dataOperation.isNotMemoryLock());
    pins.getMx().writeBool(timeline, isMemory8Bit());
//    pins.getE().writeBool(timeline, isEmulation());
//    //pins.getRdy().writeBool(timeline, dataOperation.isReady());
//    pins.getVda().writeBool(timeline, dataOperation.isValidDataAddress());
//    pins.getVpa().writeBool(timeline, dataOperation.isValidProgramAddress());
//    pins.getVpB().writeBool(timeline, dataOperation.isNotVectorPull());
//    pins.writeAddress(timeline, address.getOffset());

    executeOperation();

    if (!read)
    {
      pins.writeData(timeline, data);
    }

    cycle();
  }

  private void cycle()
  {
    if (nextInstruction)
    {
      cycle = -1;
      nextInstruction = false;
      nextInstruction();
    }

    nextCycle();
    while (!getBusCycle().mustExecute(this))
    {
      nextCycle();
    }
  }

  private void executeOperation()
  {
    BusCycle busCycle = getBusCycle();

    for (Operation operation : busCycle.getOperations())
    {
      operation.execute(this);
    }
  }

  public BusCycle getBusCycle()
  {
    InstructionCycles cycles = opCode.getCycles();
    return cycles.getBusCycle(cycle);
  }

  public boolean isStopped()
  {
    return stopped;
  }

  public boolean isRead()
  {
    return getDataOperation().isRead();
  }

  private DataOperation getDataOperation()
  {
    BusCycle busCycle = getBusCycle();
    if (busCycle != null)
    {
      return busCycle.getDataOperation();
    }
    else
    {
      System.out.println("W65C816.getDataOperation: BusCycle for OpCode [" + opCode.getName() + "] Cycle [" + cycle + "] cannot be fetch.  OpCode cycles size [" + opCode.getCycles().size() + "].");
      return null;
    }
  }

  public boolean isMemory8Bit()
  {
    if (isEmulation())
    {
      return true;
    }
    else
    {
      return accumulatorWidthFlag;
    }
  }

  public boolean isMemory16Bit()
  {
    return !isMemory8Bit();
  }

  public boolean isIndex8Bit()
  {
    if (isEmulation())
    {
      return true;
    }
    else
    {
      return indexWidthFlag;
    }
  }

  public boolean isIndex16Bit()
  {
    return !isIndex8Bit();
  }

  public boolean isCarrySet()
  {
    return isCarry();
  }

  protected int getCarry()
  {
    return isCarrySet() ? 1 : 0;
  }

  private boolean isSignSet()
  {
    return isNegative();
  }

  private boolean isOverflowSet()
  {
    return isOverflowFlag();
  }

  public void setProgramCounter(Address address)
  {
    programCounter = address;
  }

  public void setProgramAddressBank(int bank)
  {
    assert8Bit(bank, "Program Address Bank");
    programCounter.bank = bank;
  }

  public void setAddressLow(int addressLow)
  {
    assert8Bit(addressLow, "Address Low");
    this.address.setOffsetLow(addressLow);
  }

  public void setAddressHigh(int addressHigh)
  {
    assert8Bit(addressHigh, "Address High");
    this.address.setOffsetHigh(addressHigh);
  }

  public void setAddressBank(int addressBank)
  {
    assert8Bit(addressBank, "Address Bank");
    this.address.setBank(addressBank);
  }

  public int getDataLow()
  {
    return getLowByte(internal16BitData);
  }

  public int getDataHigh()
  {
    return getHighByte(internal16BitData);
  }

  public int getInternal16BitData()
  {
    if (isMemory16Bit())
    {
      return internal16BitData;
    }
    else
    {
      return toByte(internal16BitData);
    }
  }

  public int getIndexData()
  {
    if (isIndex16Bit())
    {
      return internal16BitData;
    }
    else
    {
      return toByte(internal16BitData);
    }
  }

  public int getData16Bit()
  {
    return internal16BitData;
  }

  public int getDirectPage()
  {
    return directPage;
  }

  public int getDirectOffset()
  {
    return directOffset;
  }

  public int getStackPointer()
  {
    if (!isEmulation())
    {
      return stackPointer;
    }
    else
    {
      return getLowByte(stackPointer) | 0x100;
    }
  }

  public void doneInstruction()
  {
    nextInstruction = true;
  }

  private void nextInstruction()
  {
    if (!abort)
    {
      createAbortValues();
    }

    if (nmi)
    {
      opCode = nmiOpcode;
      nmi = false;
    }
    else if (abort)
    {
      opCode = abortOpcode;
      abort = false;
    }
    else if (irq && !interruptDisableFlag)
    {
      opCode = irqOpcode;
    }
    else
    {
      opCode = fetchNextOpcode;
    }
  }

  public void nextCycle()
  {
    cycle++;
  }

  public int getCycle()
  {
    return cycle;
  }

  public void setOpCode(int data)
  {
    if ((data >= 0) && (data <= 255))
    {
      opCode = opCodeTable[data];
    }
    else
    {
      throw new SimulatorException("Invalid Op-code");
    }
  }

  public Address getAddress()
  {
    return address;
  }

  public Instruction getOpCode()
  {
    return opCode;
  }

  public Address getNewProgramCounter()
  {
    return newProgramCounter;
  }

  public void incrementProgramAddress()
  {
    this.programCounter.offset(1, true);
  }

  public void decrementProgramCounter()
  {
    this.programCounter.offset(-1, true);
  }

  public void incrementStackPointer()
  {
    stackPointer = toShort(++stackPointer);
  }

  public void decrementStackPointer()
  {
    stackPointer = toShort(--stackPointer);
  }

  public void setDirectOffset(int data)
  {
    assert8Bit(data, "Direct Offset");
    directOffset = data;
  }

  public void setDataLow(int data)
  {
    assert8Bit(data, "Data Low");
    this.internal16BitData = setLowByte(this.internal16BitData, data);
  }

  public void setDataHigh(int data)
  {
    assert8Bit(data, "Data High");
    this.internal16BitData = setHighByte(this.internal16BitData, data);
  }

  public void setStackPointer(int data)
  {
    assert16Bit(data, "Stack Pointer");
    stackPointer = data;
  }

  public void setNewProgramCounterLow(int data)
  {
    assert8Bit(data, "Program Counter Low");
    newProgramCounter.setOffsetLow(data);

  }

  public void setNewProgramCounterHigh(int data)
  {
    assert8Bit(data, "Program Counter High");
    newProgramCounter.setOffsetHigh(data);
  }

  public void setNewProgramCounterBank(int data)
  {
    assert8Bit(data, "Program Counter Bank");
    newProgramCounter.setBank(data);
  }

  public void setEmulationMode(boolean emulation)
  {
    setEmulationFlag(emulation);
    if (emulation)
    {
      xIndex = toByte(xIndex);
      yIndex = toByte(yIndex);
      setAccumulatorWidthFlag(true);
      setIndexWidthFlag(true);
      stackPointer = toByte(stackPointer) | 0x100;
    }
  }

  public boolean is8bitValueNegative(int value)
  {
    return (value & 0x80) != 0;
  }

  public boolean is16bitValueNegative(int value)
  {
    return (value & 0x8000) != 0;
  }

  public boolean is8bitValueZero(int value)
  {
    return (toByte(value) == 0);
  }

  public boolean is16bitValueZero(int value)
  {
    return (toShort(value) == 0);
  }

  private void processorStatusChanged()
  {
    if (indexWidthFlag)
    {
      xIndex = toByte(xIndex);
      yIndex = toByte(yIndex);
    }
    if (isEmulation())
    {
      setIndexWidthFlag(true);
      setAccumulatorWidthFlag(true);
    }
  }

  private int setBit(int value, boolean bitValue, int bitNumber)
  {
    if (bitValue)
    {
      value = setBit(value, bitNumber);
    }
    else
    {
      value = clearBit(value, bitNumber);
    }
    return value;
  }

  public int clearBit(int value, int bitNumber)
  {
    return trimMemory(value & ~(1 << bitNumber));
  }

  public int setBit(int value, int bitNumber)
  {
    return trimMemory(value | (1 << bitNumber));
  }

  public void incrementA()
  {
    int a = trimMemory(getA() + 1);
    setA(a);
    setSignAndZeroFromMemory(a);
  }

  public void incrementX()
  {
    int x = trimIndex(getX() + 1);
    setX(x);
    setSignAndZeroFromIndex(x);
  }

  public void incrementY()
  {
    int y = trimIndex(getY() + 1);
    setY(y);
    setSignAndZeroFromIndex(y);
  }

  public void decrementA()
  {
    int a = trimMemory(getA() - 1);
    setA(a);
    setSignAndZeroFromMemory(a);
  }

  public void decrementY()
  {
    int y = trimIndex(getY() - 1);
    setY(y);
    setSignAndZeroFromIndex(y);
  }

  public void decrementX()
  {
    int x = trimIndex(getX() - 1);
    setX(x);
    setSignAndZeroFromIndex(x);
  }

  int rotateLeft(int value)
  {
    boolean carryWillBeSet = isMemoryNegative(value);
    value = trimMemory(value << 1);
    value = setBit(value, isCarrySet(), 0);
    setCarryFlag(carryWillBeSet);
    return value;
  }

  int rotateRight(int value)
  {
    boolean carryWillBeSet = (value & 1) != 0;
    value = trimMemory(value >> 1);
    value = setBit(value, isCarrySet(), isMemory16Bit() ? 15 : 7);
    setCarryFlag(carryWillBeSet);
    return value;
  }

  int shiftRight(int value)
  {
    setCarryFlag((value & 1) != 0);
    return trimMemory(value >> 1);
  }

  public BCDResult bcdAdd8Bit(int bcdFirst, int bcdSecond, boolean carry)
  {
    int shift = 0;
    int result = 0;

    while (shift < 8)
    {
      int digitOfFirst = (bcdFirst & 0xF);
      int digitOfSecond = (bcdSecond & 0xF);
      int sumOfDigits = toByte(digitOfFirst + digitOfSecond + (carry ? 1 : 0));
      carry = sumOfDigits > 9;
      if (carry)
      {
        sumOfDigits += 6;
      }
      sumOfDigits &= 0xF;
      result |= sumOfDigits << shift;

      shift += 4;
      bcdFirst >>= shift;
      bcdSecond >>= shift;
    }

    return new BCDResult(result, carry);
  }

  public BCDResult bcdAdd16Bit(int bcdFirst, int bcdSecond, boolean carry)
  {
    int result = 0;
    int shift = 0;
    while (shift < 16)
    {
      int digitOfFirst = (bcdFirst & 0xFF);
      int digitOfSecond = (bcdSecond & 0xFF);
      BCDResult bcd8BitResult = bcdAdd8Bit(digitOfFirst, digitOfSecond, carry);
      carry = bcd8BitResult.carry;
      int partialResult = bcd8BitResult.value;
      result = toShort(result | (partialResult << shift));
      shift += 8;
      bcdFirst = toShort(bcdFirst >> shift);
      bcdSecond = toShort(bcdSecond >> shift);
    }
    return new BCDResult(result, carry);
  }

  public BCDResult bcdSubtract8Bit(int bcdFirst, int bcdSecond, boolean borrow)
  {
    int shift = 0;
    int result = 0;

    while (shift < 8)
    {
      int digitOfFirst = (bcdFirst & 0xF);
      int digitOfSecond = (bcdSecond & 0xF);
      int diffOfDigits = toByte(digitOfFirst - digitOfSecond - (borrow ? 1 : 0));
      borrow = diffOfDigits > 9;
      if (borrow)
      {
        diffOfDigits -= 6;
      }
      diffOfDigits &= 0xF;
      result |= diffOfDigits << shift;

      shift += 4;
      bcdFirst >>= shift;
      bcdSecond >>= shift;
    }

    return new BCDResult(result, borrow);
  }

  public BCDResult bcdSubtract16Bit(int bcdFirst, int bcdSecond, boolean borrow)
  {
    int result = 0;
    int shift = 0;
    while (shift < 16)
    {
      int digitOfFirst = (bcdFirst & 0xFF);
      int digitOfSecond = (bcdSecond & 0xFF);
      BCDResult bcd8BitResult = bcdSubtract8Bit(digitOfFirst, digitOfSecond, borrow);
      borrow = bcd8BitResult.carry;
      int partialResult = bcd8BitResult.value;
      result = toShort(result | (partialResult << shift));
      shift += 8;
      bcdFirst = toShort(bcdFirst >> shift);
      bcdSecond = toShort(bcdSecond >> shift);
    }
    return new BCDResult(result, borrow);
  }

  protected void execute8BitADC()
  {
    int operand = getInternal16BitData();
    int accumulator = getA();
    int carryValue = getCarry();

    int result = accumulator + operand + carryValue;

    accumulator &= 0x7F;
    operand &= 0x7F;
    boolean carryOutOfPenultimateBit = isMemoryNegative(trimMemory(accumulator + operand + carryValue));

    boolean carry = (result & 0x0100) != 0;
    setCarryFlag(carry);
    setOverflowFlag(carry ^ carryOutOfPenultimateBit);
    setA(trimMemory(result));
  }

  protected void execute16BitADC()
  {
    int operand = getInternal16BitData();
    int accumulator = getA();
    int carryValue = getCarry();

    int result = accumulator + operand + carryValue;

    accumulator &= 0x7FFF;
    operand &= 0x7FFF;
    boolean carryOutOfPenultimateBit = isMemoryNegative(trimMemory(accumulator + operand + carryValue));

    boolean carry = (result & 0x010000) != 0;
    setCarryFlag(carry);
    setOverflowFlag(carry ^ carryOutOfPenultimateBit);
    setA(trimMemory(result));
  }

  protected void execute8BitBCDADC()
  {
    int operand = getDataLow();
    int accumulator = getA();

    BCDResult bcdResult = bcdAdd8Bit(operand, accumulator, isCarrySet());
    setCarryFlag(bcdResult.carry);
    setA(bcdResult.value);
  }

  protected void execute16BitBCDADC()
  {
    int operand = getInternal16BitData();
    int accumulator = getA();

    BCDResult bcdResult = bcdAdd16Bit(operand, accumulator, isCarrySet());
    setCarryFlag(bcdResult.carry);
    setA(bcdResult.value);
  }

  public void execute8BitSBC()
  {
    int value = getInternal16BitData();
    int accumulator = getA();
    int borrowValue = 1 - getCarry();

    int result = accumulator - value - borrowValue;

    accumulator &= 0x7F;
    value &= 0x7F;
    boolean borrowFromPenultimateBit = isMemoryNegative(trimMemory(accumulator - value - borrowValue));
    boolean borrow = (result & 0x0100) != 0;

    setCarryFlag(!borrow);
    setOverflowFlag(borrow ^ borrowFromPenultimateBit);
    setA(trimMemory(result));
  }

  protected void execute16BitSBC()
  {
    int value = getInternal16BitData();
    int accumulator = getA();
    int borrowValue = 1 - getCarry();

    int result = accumulator - value - borrowValue;

    accumulator &= 0x7FFF;
    value &= 0x7FFF;
    boolean borrowFromPenultimateBit = isMemoryNegative(trimMemory(accumulator - value - borrowValue));
    boolean borrow = (result & 0x010000) != 0;

    setCarryFlag(!borrow);
    setOverflowFlag(borrow ^ borrowFromPenultimateBit);
    setA(trimMemory(result));
  }

  protected void execute8BitBCDSBC()
  {
    int value = getDataLow();
    int accumulator = getLowByte(getA());

    BCDResult bcdResult = bcdSubtract8Bit(value, accumulator, !isCarrySet());
    setCarryFlag(!bcdResult.carry);
    setA(bcdResult.value);
  }

  protected void execute16BitBCDSBC()
  {
    int value = getInternal16BitData();
    int accumulator = getA();

    BCDResult bcdResult = bcdSubtract16Bit(value, accumulator, !isCarrySet());
    setCarryFlag(!bcdResult.carry);
    setA(bcdResult.value);
  }

  public void blockMoveNext()
  {
    if (accumulator != 0xffff)
    {
      accumulator = toShort(--accumulator);
      xIndex = trimIndex(++xIndex);
      yIndex = trimIndex(++yIndex);
      programCounter.offset(-3, true);
    }
  }

  public void blockMovePrevious()
  {
    if (accumulator != 0xffff)
    {
      accumulator = toShort(--accumulator);
      xIndex = trimIndex(--xIndex);
      yIndex = trimIndex(--yIndex);
      programCounter.offset(-3, true);
    }
  }

  private boolean isMemoryNegative(int operand)
  {
    if (isMemory16Bit())
    {
      return is16bitValueNegative(operand);
    }
    else
    {
      return is8bitValueNegative(operand);
    }
  }

  private int trimMemory(int value)
  {
    if (isMemory16Bit())
    {
      value = toShort(value);
    }
    else
    {
      value = toByte(value);
    }
    return value;
  }

  private int trimIndex(int value)
  {
    if (isIndex16Bit())
    {
      value = toShort(value);
    }
    else if (isIndex8Bit())
    {
      value = toByte(value);
    }
    return value;
  }

  private void branch(boolean condition)
  {
    if (!condition)
    {
      doneInstruction();
    }
  }

  public void setZeroFlag(boolean zeroFlag)
  {
    this.zeroFlag = zeroFlag;
  }

  public void setNegativeFlag(boolean signFlag)
  {
    negativeFlag = signFlag;
  }

  public void setDecimalFlag(boolean decimalFlag)
  {
    this.decimalFlag = decimalFlag;
  }

  public void setInterruptDisableFlag(boolean interruptDisableFlag)
  {
    this.interruptDisableFlag = interruptDisableFlag;
  }

  public void setAccumulatorWidthFlag(boolean accumulatorWidthFlag)
  {
    this.accumulatorWidthFlag = accumulatorWidthFlag;
  }

  public void setIndexWidthFlag(boolean indexWidthFlag)
  {
    this.indexWidthFlag = indexWidthFlag;
  }

  public void setCarryFlag(boolean carryFlag)
  {
    this.carryFlag = carryFlag;
  }

  public void setEmulationFlag(boolean emulationFlag)
  {
    this.emulationFlag = emulationFlag;
  }

  public boolean isZeroFlag()
  {
    return zeroFlag;
  }

  public boolean isNegative()
  {
    return negativeFlag;
  }

  public boolean isDecimal()
  {
    return decimalFlag;
  }

  public boolean isInterruptDisable()
  {
    return interruptDisableFlag;
  }

  public boolean isCarry()
  {
    return carryFlag;
  }

  public boolean isEmulation()
  {
    return emulationFlag;
  }

  public boolean isBreak()
  {
    return breakFlag;
  }

  public boolean isOverflowFlag()
  {
    return overflowFlag;
  }

  public void setBreakFlag(boolean breakFlag)
  {
    this.breakFlag = breakFlag;
  }

  public void setOverflowFlag(boolean overflowFlag)
  {
    this.overflowFlag = overflowFlag;
  }

  public int getProcessorRegisterValue()
  {
    int value = 0;
    if (isCarry())
    {
      value |= STATUS_CARRY;
    }
    if (isZeroFlag())
    {
      value |= STATUS_ZERO;
    }
    if (isInterruptDisable())
    {
      value |= STATUS_INTERRUPT_DISABLE;
    }
    if (isDecimal())
    {
      value |= STATUS_DECIMAL;
    }
    if (emulationFlag && breakFlag)
    {
      value |= STATUS_BREAK;
    }
    if (!emulationFlag && indexWidthFlag)
    {
      value |= STATUS_INDEX_WIDTH;
    }
    if (!emulationFlag && accumulatorWidthFlag)
    {
      value |= STATUS_ACCUMULATOR_WIDTH;
    }
    if (isOverflowFlag())
    {
      value |= STATUS_OVERFLOW;
    }
    if (isNegative())
    {
      value |= STATUS_NEGATIVE;
    }

    return value;
  }

  public void setProcessorRegisterValue(int value)
  {
    setCarryFlag((value & STATUS_CARRY) != 0);
    setZeroFlag((value & STATUS_ZERO) != 0);
    setInterruptDisableFlag((value & STATUS_INTERRUPT_DISABLE) != 0);
    setDecimalFlag((value & STATUS_DECIMAL) != 0);

    if (isEmulation())
    {
      setBreakFlag((value & STATUS_BREAK) != 0);
    }
    else
    {
      setIndexWidthFlag((value & STATUS_INDEX_WIDTH) != 0);
    }

    setAccumulatorWidthFlag(!isEmulation() && ((value & STATUS_ACCUMULATOR_WIDTH) != 0));
    setOverflowFlag((value & STATUS_OVERFLOW) != 0);

    setNegativeFlag((value & STATUS_NEGATIVE) != 0);
  }

  public void setZeroFlagFrom8BitValue(int value)
  {
    setZeroFlag(is8bitValueZero(value));
  }

  public void setZeroFlagFrom16BitValue(int value)
  {
    setZeroFlag(is16bitValueZero(value));
  }

  public void setNegativeFlagFrom8BitValue(int value)
  {
    setNegativeFlag(is8bitValueNegative(value));
  }

  public void setNegativeFlagFrom16BitValue(int value)
  {
    setNegativeFlag(is16bitValueNegative(value));
  }

  public void setSignAndZeroFlagFrom8BitValue(int value)
  {
    setNegativeFlagFrom8BitValue(value);
    setZeroFlagFrom8BitValue(value);
  }

  public void setSignAndZeroFlagFrom16BitValue(int value)
  {
    setNegativeFlagFrom16BitValue(value);
    setZeroFlagFrom16BitValue(value);
  }

  public void ASL()
  {
    int operand = getInternal16BitData();
    boolean carry = isMemoryNegative(operand);
    operand = trimMemory(operand << 1);
    setCarryFlag(carry);
    setInternal16BitData(operand);
  }

  public void ASL_A()
  {
    int operand = getA();
    boolean carry = isMemoryNegative(operand);
    operand = trimMemory(operand << 1);
    setCarryFlag(carry);
    setA(operand);
  }

  public void PER()
  {
    internal16BitData = toShort(internal16BitData + programCounter.getOffset());  // + Carry?
  }

  public void PLD()
  {
    setDirectPage(getData16Bit());
  }

  public void PHD()
  {
    internal16BitData = directPage;
  }

  public void PLB()
  {
    setDataBank(getDataLow());
  }

  public void PHB()
  {
    setDataLow(getDataBank());
  }

  public void PHK()
  {
    setDataLow(getProgramCounter().getBank());
  }

  public void PLP()
  {
    setProcessorRegisterValue(getDataLow());
    processorStatusChanged();
  }

  public void PHP()
  {
    setDataLow(getProcessorRegisterValue());
  }

  public void BRK()
  {
    setInterruptDisableFlag(true);
    setDecimalFlag(false);
    if (isEmulation())
    {
      setBreakFlag(true);
    }
  }

  public void COP()
  {
    setInterruptDisableFlag(true);
    setDecimalFlag(false);
    if (isEmulation())
    {
      setBreakFlag(true);
    }
  }

  public void ORA()
  {
    setA(getA() | getInternal16BitData());
  }

  public void TSB()
  {
    int value = getInternal16BitData();
    setData((value | getA()), false);
    setZeroFlag((value & getA()) == 0);
  }

  public void TRB()
  {
    int value = getInternal16BitData();
    setData(value & trimMemory(~getA()), false);
    setZeroFlag((value & getA()) == 0);
  }

  public void BPL()
  {
    branch(!isSignSet());
  }

  public void BMI()
  {
    branch(isSignSet());
  }

  public void CLC()
  {
    setCarryFlag(false);
  }

  public void INC_A()
  {
    incrementA();
  }

  public void TCS()
  {
    int stackPointer = getStackPointer();
    if (isEmulation())
    {
      stackPointer = setLowByte(stackPointer, getLowByte(getA()));
    }
    else
    {
      stackPointer = getA();
    }
    setStackPointer(stackPointer);
  }

  public void AND()
  {
    setA((getA() & getInternal16BitData()));
  }

  public void BIT()
  {
    int value = getInternal16BitData();

    if (isMemory16Bit())
    {
      setNegativeFlag(is16bitValueNegative(value));
      setOverflowFlag((value & 0x4000) != 0);
      setZeroFlagFrom16BitValue((value & getA()));
    }
    else
    {
      setNegativeFlag(is8bitValueNegative(value));
      setOverflowFlag((value & 0x40) != 0);
      setZeroFlagFrom8BitValue((value & getA()));
    }
  }

  public void BIT_I()
  {
    if (isMemory16Bit())
    {
      setZeroFlagFrom16BitValue((getInternal16BitData() & getA()));
    }
    else
    {
      setZeroFlagFrom8BitValue((getInternal16BitData() & getA()));
    }
  }

  public void ROL()
  {
    setInternal16BitData(rotateLeft(getInternal16BitData()));
  }

  public void ROL_A()
  {
    setA(rotateLeft(getA()));
  }

  public void SEC()
  {
    setCarryFlag(true);
  }

  public void DEC_A()
  {
    decrementA();
  }

  public void TSC()
  {
    setA(getStackPointer());
    setSignAndZeroFromMemory(getA());
  }

  public void EOR()
  {
    int result = trimMemory(getA() ^ getInternal16BitData());
    setSignAndZeroFromMemory(result);
    setA(result);
  }

  public void WDM()
  {
  }

  public void MVP()
  {
    blockMovePrevious();
  }

  public void MVN()
  {
    blockMoveNext();
  }

  public void LSR()
  {
    setInternal16BitData(shiftRight(getInternal16BitData()));
  }

  public void PHA()
  {
    setData(getA(), false);
  }

  public void LSR_A()
  {
    setA(shiftRight(getA()));
  }

  public void BVC()
  {
    branch(!isOverflowSet());
  }

  public void CLI()
  {
    setInterruptDisableFlag(false);
  }

  public void PHY()
  {
    setIndexData(getY(), false);
  }

  public void TCD()
  {
    setDirectPage(accumulator);
    setSignAndZeroFlagFrom16BitValue(accumulator);
  }

  public void ADC()
  {
    if (isMemory16Bit())
    {
      if (!isDecimal())
      {
        execute16BitADC();
      }
      else
      {
        execute16BitBCDADC();
      }
    }
    else
    {
      if (!isDecimal())
      {
        execute8BitADC();
      }
      else
      {
        execute8BitBCDADC();
      }
    }
  }

  public void STZ()
  {
    setInternal16BitData(0);
  }

  public void PLA()
  {
    setA(getInternal16BitData());
  }

  public void ROR()
  {
    setInternal16BitData(rotateRight(getInternal16BitData()));
  }

  public void ROR_A()
  {
    setA(rotateRight(getA()));
  }

  public void BVS()
  {
    branch(isOverflowFlag());
  }

  public void SEI()
  {
    setInterruptDisableFlag(true);
  }

  public void PLY()
  {
    setY(getIndexData());
  }

  public void TDC()
  {
    setC(getDirectPage());
  }

  public void BRA()
  {
    branch(true);
  }

  public void STA()
  {
    setData(getA(), false);
  }

  public void STY()
  {
    setIndexData(getY(), false);
  }

  public void STX()
  {
    setIndexData(getX(), false);
  }

  public void DEY()
  {
    decrementY();
  }

  public void TXA()
  {
    if (isMemory8Bit() && isIndex16Bit())
    {
      setA(getLowByte(getX()));
    }
    else if (isMemory16Bit() && isIndex8Bit())
    {
      setA(setLowByte(getA(), getX()));
    }
    else
    {
      setA(getX());
    }
  }

  public void BCC()
  {
    branch(!isCarry());
  }

  public void LDY()
  {
    if (isIndex16Bit())
    {
      setY(internal16BitData);
    }
    else
    {
      setY(toByte(internal16BitData));
    }
  }

  public void LDA()
  {
    setA(getInternal16BitData());
  }

  public void LDX()
  {
    if (isIndex16Bit())
    {
      setX(internal16BitData);
    }
    else
    {
      setX(toByte(internal16BitData));
    }
  }

  public void BCS()
  {
    branch(isCarry());
  }

  public void CLV()
  {
    setOverflowFlag(false);
  }

  public void TSX()
  {
    int stackPointer = getStackPointer();
    if (isIndex8Bit())
    {
      int stackPointerLower8Bits = getLowByte(stackPointer);
      setX(setLowByte(getX(), stackPointerLower8Bits));
    }
    else
    {
      setX(stackPointer);
    }
  }

  public void TYX()
  {
    setX(getY());
  }

  public void TYA()
  {
    if (isMemory8Bit() && isIndex16Bit())
    {
      setA(getLowByte(getY()));
    }
    else if (isMemory16Bit() && isIndex8Bit())
    {
      setA(setLowByte(getA(), getY()));
    }
    else
    {
      setA(getY());
    }
  }

  public void TXS()
  {
    if (isEmulation())
    {
      int newStackPointer = 0x100 | getLowByte(getX());
      setStackPointer(newStackPointer);
    }
    else
    {
      setStackPointer(getX());
    }
  }

  public void TXY()
  {
    setY(getX());
  }

  public void CPY()
  {
    int value = getIndexData();
    setSignAndZeroFromIndex(trimMemory(getY() - value));
    setCarryFlag(getY() >= value);
  }

  public void CMP()
  {
    int value = getInternal16BitData();
    setSignAndZeroFromMemory(trimMemory(getA() - value));
    setCarryFlag(getA() >= value);
  }

  public void REP()
  {
    int value = toByte(~getDataLow());
    setProcessorRegisterValue(getProcessorRegisterValue() & value);
    processorStatusChanged();
  }

  public void TAY()
  {
    if ((isMemory8Bit() && isIndex8Bit()) ||
        (isMemory16Bit() && isIndex8Bit()))
    {
      int lower8BitsOfA = getLowByte(getA());
      setY(setLowByte(getY(), lower8BitsOfA));
    }
    else
    {
      setY(getA());
    }
  }

  public void TAX()
  {
    if ((isMemory8Bit() && isIndex8Bit()) ||
        (isMemory16Bit() && isIndex8Bit()))
    {
      int lower8BitsOfA = getLowByte(getA());
      setX(setLowByte(getX(), lower8BitsOfA));
    }
    else
    {
      setX(getA());
    }
  }

  public void PHX()
  {
    setIndexData(getX(), false);
  }

  public void STP()
  {
    stopped = true;
  }

  public void DEC()
  {
    setInternal16BitData(trimMemory(getInternal16BitData() - 1));
  }

  public void INY()
  {
    incrementY();
  }

  public void DEX()
  {
    decrementX();
  }

  public void BNE()
  {
    branch(!isZeroFlag());
  }

  public void CLD()
  {
    setDecimalFlag(false);
  }

  public void CPX()
  {
    int value = getIndexData();
    setSignAndZeroFromIndex(trimMemory(getX() - value));
    setCarryFlag(getX() >= value);
  }

  public void SBC()
  {
    if (isMemory16Bit())
    {
      if (!isDecimal())
      {
        execute16BitSBC();
      }
      else
      {
        execute16BitBCDSBC();
      }
    }
    else
    {
      if (!isDecimal())
      {
        execute8BitSBC();
      }
      else
      {
        execute8BitBCDSBC();
      }
    }
  }

  public void SEP()
  {
    int value = getDataLow();
    if (isEmulation())
    {
      value = value & 0xCF;
    }
    setProcessorRegisterValue(getProcessorRegisterValue() | value);
    processorStatusChanged();
  }

  public void INC()
  {
    setInternal16BitData(trimMemory(getInternal16BitData() + 1));
  }

  public void INX()
  {
    incrementX();
  }

  public void NOP()
  {
  }

  public void XBA()
  {
    int lowerA = IntUtil.getLowByte(getA());
    int higherA = IntUtil.getHighByte(getA());
    accumulator = higherA | (lowerA << 8);
  }

  public void BEQ()
  {
    branch(isZeroFlag());
  }

  public void SED()
  {
    setDecimalFlag(true);
  }

  public void PLX()
  {
    setX(getIndexData());
  }

  public void XCE()
  {
    boolean oldCarry = isCarry();
    boolean oldEmulation = isEmulation();
    setEmulationMode(oldCarry);
    setCarryFlag(oldEmulation);

    setAccumulatorWidthFlag(isEmulation());
    setIndexWidthFlag(isEmulation());
  }

  public void ABORT()
  {
    setInterruptDisableFlag(true);
    setDecimalFlag(false);
  }

  public void IRQ()
  {
    setInterruptDisableFlag(true);
    setDecimalFlag(false);
  }

  public void NMI()
  {
    setInterruptDisableFlag(true);
    setDecimalFlag(false);
  }

  public void RES()
  {
    setEmulationMode(true);
    dataBank = 0;
    directPage = 0;
    programCounter.setBank(0);
    setDecimalFlag(false);
    setInterruptDisableFlag(true);
  }

  public String getAddressValueHex()
  {
    return getByteStringHex(getAddress().getBank()) + ":" + to16BitHex(getAddress().getOffset());
  }

  public String getAccumulatorValueHex()
  {
    return getWordStringHex(getA());
  }

  public String getXValueHex()
  {
    return getWordStringHex(getX());
  }

  public String getYValueHex()
  {
    return getWordStringHex(getY());
  }

  public String getDataBankValueHex()
  {
    return getByteStringHex(getDataBank());
  }

  public String getStackValueHex()
  {
    return getWordStringHex(getStackPointer());
  }

  public String getDirectPageValueHex()
  {
    return getWordStringHex(getDirectPage());
  }

  public String getProgramCounterValueHex()
  {
    return getByteStringHex(getProgramCounter().getBank()) + ":" + to16BitHex(getProgramCounter().getOffset());
  }

  public String getDataValueHex()
  {
    return getWordStringHex(getData16Bit());
  }

  public String getOpcodeValueHex()
  {
    return getOpcodeValueHex(getCycle(), getOpCode());
  }

  public String getOpcodeValueHex(int cycle, Instruction opCode)
  {
    if (cycle != 0)
    {
      int code = opCode.getCode();
      if (code >= 0 && code <= 255)
      {
        return getByteStringHex(code);
      }
      else
      {
        return "---";
      }
    }
    else
    {
      return "###";
    }
  }

  public String getOpcodeMnemonicString()
  {
    return opCode.getName();
  }

  private String getStatusString()
  {
    String z = isZeroFlag() ? "Z:1 " : "Z:0 ";
    String n = isNegative() ? "N:1 " : "N:0 ";
    String d = isDecimal() ? "D:1 " : "D:0 ";
    String i = isInterruptDisable() ? "I:1 " : "I:0 ";
    String m = isMemory8Bit() ? "M8  " : "M16 ";
    String x = "";
    if (!isEmulation())
    {
      x = isIndex8Bit() ? "X8  " : "X16 ";
    }
    String c = isCarry() ? "C1 " : "C0 ";
    String e = isEmulation() ? "E1 " : "E0 ";
    String o = isOverflowFlag() ? "O1 " : "O0 ";
    String b = "";
    if (isEmulation())
    {
      b = isBreak() ? "B1 " : "B0 ";
    }
    return z + n + d + i + m + x + c + e + o + b;
  }

  public boolean isBusEnable()
  {
    return busEnable;
  }

  public String getType()
  {
    return TYPE;
  }

  public String getCycleString()
  {
    return Integer.toString(cycle);
  }

  public void setData(int data)
  {
    this.data = data;
  }

  public int getData()
  {
    return data;
  }
}

