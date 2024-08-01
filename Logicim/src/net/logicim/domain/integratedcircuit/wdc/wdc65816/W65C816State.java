package net.logicim.domain.integratedcircuit.wdc.wdc65816;

import net.common.SimulatorException;
import net.common.util.IntUtil;
import net.logicim.domain.common.state.State;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.BusCycle;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.Instruction;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.InstructionFactory;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address.InstructionCycles;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations.DataOperation;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations.Operation;

import static net.common.util.IntUtil.*;
import static net.logicim.domain.integratedcircuit.wdc.wdc65816.CpuFlags.*;

public class W65C816State
    extends State
{
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
  protected int opCodeIndex;
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

  public W65C816State()
  {
    opCodeTable = InstructionFactory.getInstance().getInstructions();
    resetOpcode = InstructionFactory.getInstance().getReset();
    irqOpcode = InstructionFactory.getInstance().getIRQ();
    nmiOpcode = InstructionFactory.getInstance().getNMI();
    abortOpcode = InstructionFactory.getInstance().getAbort();
    fetchNextOpcode = InstructionFactory.getInstance().getFetchNext();

    programCounter = new Address(0x00, 0x0000);
    stackPointer = 0x01FF;
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
    opCodeIndex = resetOpcode.getCode();
    internal16BitData = 0;
    directOffset = 0;
    newProgramCounter = new Address();
    address = new Address();
    nmi = false;

    createAbortValues();
  }

  public W65C816State(W65C816State state)
  {
    opCodeTable = InstructionFactory.getInstance().getInstructions();
    resetOpcode = InstructionFactory.getInstance().getReset();
    irqOpcode = InstructionFactory.getInstance().getIRQ();
    nmiOpcode = InstructionFactory.getInstance().getNMI();
    abortOpcode = InstructionFactory.getInstance().getAbort();
    fetchNextOpcode = InstructionFactory.getInstance().getFetchNext();

    this.zeroFlag = state.zeroFlag;
    this.negativeFlag = state.negativeFlag;
    this.decimalFlag = state.decimalFlag;
    this.interruptDisableFlag = state.interruptDisableFlag;
    this.accumulatorWidthFlag = state.accumulatorWidthFlag;
    this.indexWidthFlag = state.indexWidthFlag;
    this.carryFlag = state.carryFlag;
    this.emulationFlag = state.emulationFlag;
    this.overflowFlag = state.overflowFlag;
    this.breakFlag = state.breakFlag;
    this.accumulator = state.accumulator;
    this.xIndex = state.xIndex;
    this.yIndex = state.yIndex;
    this.dataBank = state.dataBank;
    this.directPage = state.directPage;
    this.programCounter = new Address(state.programCounter);
    this.stackPointer = state.stackPointer;
    this.cycle = state.cycle;
    this.opCodeIndex = state.opCodeIndex;
    this.stopped = state.stopped;
    this.abortProcessRegister = state.abortProcessRegister;
    this.abortAccumulator = state.abortAccumulator;
    this.abortXIndex = state.abortXIndex;
    this.abortYIndex = state.abortYIndex;
    this.abortDataBank = state.abortDataBank;
    this.abortDirectPage = state.abortDirectPage;
    this.abortProgramCounter = new Address(state.abortProgramCounter);
    this.abortStackPointer = state.abortStackPointer;
    this.address = new Address(state.address);
    this.internal16BitData = state.internal16BitData;
    this.directOffset = state.directOffset;
    this.newProgramCounter = new Address(state.newProgramCounter);
    this.busEnable = state.busEnable;
    this.irq = state.irq;
    this.abort = state.abort;
    this.nextInstruction = state.nextInstruction;
    this.data = state.data;
    this.nmi = state.nmi;
  }

  public void createAbortValues()
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

  public void resetPulled()
  {
    abort = false;
    nmi = false;
    opCodeIndex = resetOpcode.getCode();
    stopped = false;
    cycle = 0;
  }

  public int getProcessorRegisterValue()
  {
    int value = 0;
    if (isCarrySet())
    {
      value |= STATUS_CARRY;
    }
    if (isZeroFlagSet())
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
    if (isNegativeSet())
    {
      value |= STATUS_NEGATIVE;
    }

    return value;
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

  public boolean isZeroFlagSet()
  {
    return zeroFlag;
  }

  public boolean isNegativeSet()
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

  public boolean isCarrySet()
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

  public void cycle(W65C816 cpu)
  {
    if (nextInstruction)
    {
      cycle = -1;
      nextInstruction = false;
      nextInstruction();
    }

    nextCycle();
    while (!getBusCycle().mustExecute(cpu))
    {
      nextCycle();
    }
  }

  public void executeOperation(W65C816 cpu)
  {
    BusCycle busCycle = getBusCycle();

    for (Operation operation : busCycle.getOperations())
    {
      operation.execute(cpu);
    }
  }

  public BusCycle getBusCycle()
  {
    Instruction instruction = opCodeTable[opCodeIndex];
    InstructionCycles cycles = instruction.getCycles();
    return cycles.getBusCycle(cycle);
  }

  public void nextInstruction()
  {
    if (!abort)
    {
      createAbortValues();
    }

    if (nmi)
    {
      opCodeIndex = nmiOpcode.getCode();
      nmi = false;
    }
    else if (abort)
    {
      opCodeIndex = abortOpcode.getCode();
      abort = false;
    }
    else if (irq && !interruptDisableFlag)
    {
      opCodeIndex = irqOpcode.getCode();
    }
    else
    {
      opCodeIndex = fetchNextOpcode.getCode();
    }
  }

  public void doneInstruction()
  {
    nextInstruction = true;
  }

  public void nextCycle()
  {
    cycle++;
  }

  public int getCycle()
  {
    return cycle;
  }

  public void setOpCode(int opCodeIndex)
  {
    if ((opCodeIndex >= 0) && (opCodeIndex <= 255))
    {
      this.opCodeIndex = opCodeIndex;
    }
    else
    {
      throw new SimulatorException("Invalid Op-code");
    }
  }

  public DataOperation getDataOperation()
  {
    BusCycle busCycle = getBusCycle();
    if (busCycle != null)
    {
      return busCycle.getDataOperation();
    }
    else
    {
      Instruction instruction = opCodeTable[opCodeIndex];
      System.out.println("W65C816.getDataOperation: BusCycle for OpCode [" + instruction.getName() + "] Cycle [" + cycle + "] cannot be fetch.  OpCode cycles size [" + instruction.getCycles().size() + "].");
      return null;
    }
  }

  public boolean isStopped()
  {
    return this.stopped;
  }

  public boolean isRead()
  {
    return getDataOperation().isRead();
  }

  public boolean isMemory8Bit()
  {
    if (isEmulation())
    {
      return true;
    }
    else
    {
      return this.accumulatorWidthFlag;
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
      return this.indexWidthFlag;
    }
  }

  public void setX(int xIndex)
  {
    if (isIndex16Bit())
    {
      assert16Bit(xIndex, "X Index register");
      this.xIndex = xIndex;
    }
    else
    {
      assert8Bit(xIndex, "X Index register");
      this.xIndex = setLowByte(this.xIndex, xIndex);
    }
    setSignAndZeroFromIndex(xIndex);
  }

  public void setY(int yIndex)
  {
    if (isIndex16Bit())
    {
      assert16Bit(yIndex, "Y Index register");
      this.yIndex = yIndex;
    }
    else
    {
      assert8Bit(yIndex, "Y Index register");
      this.yIndex = setLowByte(this.yIndex, yIndex);
    }
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

  public void setSignAndZeroFromMemory(int value)
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

  public void setSignAndZeroFromIndex(int value)
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
      return this.accumulator;
    }
    else
    {
      return toByte(this.accumulator);
    }
  }

  public int getC()
  {
    return this.accumulator;
  }

  public int getX()
  {
    if (isIndex16Bit())
    {
      return this.xIndex;
    }
    else
    {
      return toByte(this.xIndex);
    }
  }

  public int getY()
  {
    if (isIndex16Bit())
    {
      return this.yIndex;
    }
    else
    {
      return toByte(this.yIndex);
    }
  }

  public int getDataBank()
  {
    return this.dataBank;
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
    return this.programCounter;
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

  public boolean isMemoryNegative(int operand)
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

  public int trimMemory(int value)
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

  public int trimIndex(int value)
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

  public boolean isIndex16Bit()
  {
    return !isIndex8Bit();
  }

  protected int getCarry()
  {
    return isCarrySet() ? 1 : 0;
  }

  public boolean isSignSet()
  {
    return isNegativeSet();
  }

  public boolean isOverflowSet()
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

  public Address getAddress()
  {
    return address;
  }

  public Instruction getOpCode()
  {
    return opCodeTable[opCodeIndex];
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

  public void processorStatusChanged()
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

  public boolean isBusEnable()
  {
    return busEnable;
  }

  public int getData()
  {
    return data;
  }

  public void setData(int data)
  {
    this.data = data;
  }

  protected void perInternal()
  {
    internal16BitData = toShort(internal16BitData + programCounter.getOffset());  // + Carry?
  }

  protected void phdInternal()
  {
    internal16BitData = directPage;
  }

  protected void plpInternal()
  {
    setProcessorRegisterValue(getDataLow());
    processorStatusChanged();
  }

  protected void brkInternal()
  {
    setInterruptDisableFlag(true);
    setDecimalFlag(false);
    if (isEmulation())
    {
      setBreakFlag(true);
    }
  }

  protected void oraInternal()
  {
    setA(getA() | getInternal16BitData());
  }

  protected void tsbInternal()
  {
    int value = getInternal16BitData();
    setData((value | getA()), false);
    setZeroFlag((value & getA()) == 0);
  }

  protected void trbInternal()
  {
    int value = getInternal16BitData();
    setData(value & trimMemory(~getA()), false);
    setZeroFlag((value & getA()) == 0);
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

  protected void bitInternal()
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

  protected void bitIInternal()
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

  protected void rotateRightInternal()
  {
    setInternal16BitData(rotateRight(getInternal16BitData()));
  }

  protected void rotateRightAInternal()
  {
    setA(rotateRight(getA()));
  }

  protected void rotateLeftInternal()
  {
    setInternal16BitData(rotateLeft(getInternal16BitData()));
  }

  protected void rotateLeftAInternal()
  {
    setA(rotateLeft(getA()));
  }

  int shiftRight(int value)
  {
    setCarryFlag((value & 1) != 0);
    return trimMemory(value >> 1);
  }

  protected void shiftRightInternal()
  {
    setInternal16BitData(shiftRight(getInternal16BitData()));
  }

  protected void shiftRightAInternal()
  {
    setA(shiftRight(getA()));
  }

  protected void aslInternal()
  {
    int operand = getInternal16BitData();
    boolean carry = isMemoryNegative(operand);
    operand = trimMemory(operand << 1);
    setCarryFlag(carry);
    setInternal16BitData(operand);
  }

  protected void aslAInternal()
  {
    int operand = getA();
    boolean carry = isMemoryNegative(operand);
    operand = trimMemory(operand << 1);
    setCarryFlag(carry);
    setA(operand);
  }

  protected void tscInternal()
  {
    setA(getStackPointer());
    setSignAndZeroFromMemory(getA());
  }

  protected void eorInternal()
  {
    int result = trimMemory(getA() ^ getInternal16BitData());
    setSignAndZeroFromMemory(result);
    setA(result);
  }

  protected void tcdInternal()
  {
    setDirectPage(accumulator);
    setSignAndZeroFlagFrom16BitValue(accumulator);
  }

  protected void adcInternal()
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

  protected void txaInternal()
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

  protected void ldaInternal()
  {
    setA(getInternal16BitData());
  }

  protected void ldyInternal()
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

  protected void ldxInternal()
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

  protected void tsxInternal()
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

  protected void tyaInternal()
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

  protected void txsInternal()
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

  protected void cpyInternal()
  {
    int value = getIndexData();
    setSignAndZeroFromIndex(trimMemory(getY() - value));
    setCarryFlag(getY() >= value);
  }

  protected void cmpInternal()
  {
    int value = getInternal16BitData();
    setSignAndZeroFromMemory(trimMemory(getA() - value));
    setCarryFlag(getA() >= value);
  }

  protected void repInternal()
  {
    int value = toByte(~getDataLow());
    setProcessorRegisterValue(getProcessorRegisterValue() & value);
    processorStatusChanged();
  }

  protected void tayInternal()
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

  protected void taxInternal()
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

  protected void decInternal()
  {
    setInternal16BitData(trimMemory(getInternal16BitData() - 1));
  }

  protected void cpxInternal()
  {
    int value = getIndexData();
    setSignAndZeroFromIndex(trimMemory(getX() - value));
    setCarryFlag(getX() >= value);
  }

  protected void sbcInternal()
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

  protected void sepInternal()
  {
    int value = getDataLow();
    if (isEmulation())
    {
      value = value & 0xCF;
    }
    setProcessorRegisterValue(getProcessorRegisterValue() | value);
    processorStatusChanged();
  }

  protected void incInternal()
  {
    setInternal16BitData(trimMemory(getInternal16BitData() + 1));
  }

  protected void xbaInternal()
  {
    int lowerA = IntUtil.getLowByte(getA());
    int higherA = IntUtil.getHighByte(getA());
    accumulator = higherA | (lowerA << 8);
  }

  protected void xceInternal()
  {
    boolean oldCarry = isCarrySet();
    boolean oldEmulation = isEmulation();
    setEmulationMode(oldCarry);
    setCarryFlag(oldEmulation);

    setAccumulatorWidthFlag(isEmulation());
    setIndexWidthFlag(isEmulation());
  }

  protected void resetInternal()
  {
    setEmulationMode(true);
    dataBank = 0;
    directPage = 0;
    programCounter.setBank(0);
    setDecimalFlag(false);
    setInterruptDisableFlag(true);
  }

  protected void tcsInternal()
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

  protected void andInternal()
  {
    setA((getA() & getInternal16BitData()));
  }

  public void doneIfIndex8Bit()
  {
    if (isIndex8Bit())
    {
      doneInstruction();
    }
  }

  public void doneIfIndex16Bit()
  {
    if (isIndex16Bit())
    {
      doneInstruction();
    }
  }

  public void doneIfMemory8Bit()
  {
    if (isMemory8Bit())
    {
      doneInstruction();
    }
  }

  public void doneIfMemory16Bit()
  {
    if (isMemory16Bit())
    {
      doneInstruction();
    }
  }

  public void writeProcessorStatus()
  {
    setData(getProcessorRegisterValue());
  }

  public void readProcessorStatus()
  {
    setProcessorRegisterValue(getData());
  }

  public int getData16BitOffset()
  {
    int dataLow = getData16Bit();
    if (is16bitValueNegative(dataLow))
    {
      return dataLow | 0xffff0000;
    }
    else
    {
      return dataLow;
    }
  }

  public int getData8BitOffset()
  {
    int dataLow = getDataLow();
    if (is8bitValueNegative(dataLow))
    {
      return dataLow | 0xffffff00;
    }
    else
    {
      return dataLow;
    }
  }

  public void writeDataLow()
  {
    setData(getDataLow());
  }

  public void writeDataHigh()
  {
    setData(getDataHigh());
  }

  public void readOpCode()
  {
    setOpCode(getData());
  }

  public boolean noteFourX(boolean nextWillRead)
  {
    return (getLowByte(getAddress().getOffset()) + getLowByte(getX())) > 0xFF ||
           !nextWillRead ||
           isIndex16Bit();
  }

  public boolean noteFourY(boolean nextWillRead)
  {
    return (getLowByte(getAddress().getOffset()) + getLowByte(getY())) > 0xFF ||
           !nextWillRead ||
           isIndex16Bit();
  }

  public boolean noteSix()
  {
    if (isEmulation())
    {
      int pcOffset = getProgramCounter().getOffset();
      return Address.areOffsetsAreOnDifferentPages(pcOffset, pcOffset + getData16Bit());
    }
    return false;
  }

  public int getAddressOffsetX()
  {
    return toByte(getLowByte(getAddress().getOffset()) + getLowByte(getX()));
  }

  public int getAddressOffsetY()
  {
    return toByte(getLowByte(getAddress().getOffset()) + getLowByte(getY()));
  }

  public void writeProgramBank()
  {
    setData(getProgramCounter().getBank());
  }

  public void writeProgramCounterHigh()
  {
    setData(IntUtil.getHighByte(getProgramCounter().getOffset()));
  }

  public void writeProgramCounterLow()
  {
    setData(IntUtil.getLowByte(getProgramCounter().getOffset()));
  }
}

