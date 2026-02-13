package net.logicim.domain.integratedcircuit.wdc.wdc65816;

import net.logicim.domain.Simulation;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.LogicPort;
import net.logicim.domain.common.state.State;
import net.logicim.domain.common.wire.TraceValue;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.BusCycle;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.Instruction;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation.DataOperation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;

import static net.common.util.StringUtil.*;

public class W65C816
    extends IntegratedCircuit<W65C816Pins, W65C816State>
{
  public static final String TYPE = "W65C816 Microprocessor";

  public W65C816(SubcircuitSimulation containingSubcircuitSimulation,
                 String name,
                 W65C816Pins pins)
  {
    super(containingSubcircuitSimulation,
          name,
          pins);
  }

  public W65C816Pins getPins()
  {
    return pins;
  }

  protected void reset()
  {
    super.reset();
    state.resetPulled();
  }

  @Override
  public void simulationStarted(Simulation simulation)
  {
  }

  private void disableBuses()
  {
    //@todo - implement me please.
  }

  @Override
  public void inputTransition(Timeline timeline, LogicPort port)
  {
    W65C816Pins pins = getPins();
    boolean reset = pins.readRES(timeline).isLow();
    if (reset)
    {
      state.resetPulled();
    }

    TraceValue clockValue = pins.readPhi2(timeline);
    boolean nmi = pins.readNMI(timeline).isLow();
    boolean irq = pins.readIRQ(timeline).isLow();
    boolean abort = pins.readAbort(timeline).isLow();

    state.busEnable = pins.readBE(timeline).isHigh();
    if (!state.isBusEnable())
    {
      disableBuses();
    }

    if (clockValue.isHigh() || clockValue.isLow())
    {
      boolean fallingEdge = clockValue.isHigh() && state.previousClockLow;
      boolean risingEdge = clockValue.isLow() && state.previousClockHigh;

      state.previousClockLow = clockValue.isLow();
      state.previousClockHigh = clockValue.isHigh();

      if (!state.isStopped())
      {
        if (fallingEdge)
        {
          executeLowHalf(timeline);
        }

        if (risingEdge)
        {
          executeHighHalf(timeline);
        }
      }
    }

    state.nmi = !nmi;
    state.irq = !irq;
    state.abort = !abort;
  }

  public final void executeLowHalf(Timeline timeline)
  {
    DataOperation dataOperation = state.getDataOperation();
    BusCycle busCycle = state.getBusCycle();
    boolean read = dataOperation.isRead();
    Address address = busCycle.getAddress(this);

    pins.writeRWB(timeline, read);
    pins.writeMX(timeline, state.isIndex8Bit());
    pins.writeVDA(timeline, dataOperation.isValidDataAddress());
    pins.writeVPA(timeline, dataOperation.isValidProgramAddress());
    pins.writeMLB(timeline, dataOperation.isNotMemoryLock());
    pins.writeVPB(timeline, dataOperation.isNotVectorPull());
    pins.writeE(timeline, state.isEmulation());
    //pins.getRdy().writeBool(timeline, dataOperation.isReady());
    pins.writeAddress(timeline, address.getOffset());
    pins.writeData(timeline, address.getBank());
  }

  public final void executeHighHalf(Timeline timeline)
  {
    DataOperation dataOperation = state.getDataOperation();
    boolean read = dataOperation.isRead();

    if (read)
    {
      state.data = pins.readData(timeline);
    }

    pins.writeMX(timeline, state.isMemory8Bit());

    state.executeOperation(this);

    if (!read)
    {
      pins.writeData(timeline, state.getData());
    }

    state.cycle(this);
  }

  private void branch(boolean condition)
  {
    if (!condition)
    {
      state.doneInstruction();
    }
  }

  public void ASL()
  {
    state.aslInternal();
  }

  public void ASL_A()
  {
    state.aslAInternal();
  }

  public void PER()
  {
    state.perInternal();
  }

  public void PLD()
  {
    state.setDirectPage(state.getData16Bit());
  }

  public void PHD()
  {
    state.phdInternal();
  }

  public void PLB()
  {
    state.setDataBank(state.getDataLow());
  }

  public void PHB()
  {
    state.setDataLow(state.getDataBank());
  }

  public void PHK()
  {
    state.setDataLow(state.getProgramCounter().getBank());
  }

  public void PLP()
  {
    state.plpInternal();
  }

  public void PHP()
  {
    state.setDataLow(state.getProcessorRegisterValue());
  }

  public void BRK()
  {
    state.brkInternal();
  }

  public void COP()
  {
    state.brkInternal();
  }

  public void ORA()
  {
    state.oraInternal();
  }

  public void TSB()
  {
    state.tsbInternal();
  }

  public void TRB()
  {
    state.trbInternal();
  }

  public void BPL()
  {
    branch(!state.isSignSet());
  }

  public void BMI()
  {
    branch(state.isSignSet());
  }

  public void CLC()
  {
    state.setCarryFlag(false);
  }

  public void INC_A()
  {
    state.incrementA();
  }

  public void TCS()
  {
    state.tcsInternal();
  }

  public void AND()
  {
    state.andInternal();
  }

  public void BIT()
  {
    state.bitInternal();
  }

  public void BIT_I()
  {
    state.BIT_I();
  }

  public void ROL()
  {
    state.rotateLeftInternal();
  }

  public void ROL_A()
  {
    state.rotateLeftAInternal();
  }

  public void SEC()
  {
    state.setCarryFlag(true);
  }

  public void DEC_A()
  {
    state.decrementA();
  }

  public void TSC()
  {
    state.tscInternal();
  }

  public void EOR()
  {
    state.eorInternal();
  }

  public void WDM()
  {
  }

  public void MVP()
  {
    state.blockMovePrevious();
  }

  public void MVN()
  {
    state.blockMoveNext();
  }

  public void LSR()
  {
    state.shiftRightInternal();
  }

  public void PHA()
  {
    state.setData(state.getA(), false);
  }

  public void LSR_A()
  {
    state.shiftRightAInternal();
  }

  public void BVC()
  {
    branch(!state.isOverflowSet());
  }

  public void CLI()
  {
    state.setInterruptDisableFlag(false);
  }

  public void PHY()
  {
    state.setIndexData(state.getY(), false);
  }

  public void TCD()
  {
    state.tcdInternal();
  }

  public void ADC()
  {
    state.adcInternal();
  }

  public void STZ()
  {
    state.setInternal16BitData(0);
  }

  public void PLA()
  {
    state.setA(state.getInternal16BitData());
  }

  public void ROR()
  {
    state.rotateRightInternal();
  }

  public void ROR_A()
  {
    state.rotateRightAInternal();
  }

  public void BVS()
  {
    branch(state.isOverflowFlag());
  }

  public void SEI()
  {
    state.setInterruptDisableFlag(true);
  }

  public void PLY()
  {
    state.setY(state.getIndexData());
  }

  public void TDC()
  {
    state.setC(state.getDirectPage());
  }

  public void BRA()
  {
    branch(true);
  }

  public void STA()
  {
    state.setData(state.getA(), false);
  }

  public void STY()
  {
    state.setIndexData(state.getY(), false);
  }

  public void STX()
  {
    state.setIndexData(state.getX(), false);
  }

  public void DEY()
  {
    state.decrementY();
  }

  public void TXA()
  {
    state.txaInternal();
  }

  public void BCC()
  {
    branch(!state.isCarrySet());
  }

  public void LDY()
  {
    state.ldyInternal();
  }

  public void LDA()
  {
    state.ldaInternal();
  }

  public void LDX()
  {
    state.ldxInternal();
  }

  public void BCS()
  {
    branch(state.isCarrySet());
  }

  public void CLV()
  {
    state.setOverflowFlag(false);
  }

  public void TSX()
  {
    state.tsxInternal();
  }

  public void TYX()
  {
    state.setX(state.getY());
  }

  public void TYA()
  {
    state.tyaInternal();
  }

  public void TXS()
  {
    state.txsInternal();
  }

  public void TXY()
  {
    state.setY(state.getX());
  }

  public void CPY()
  {
    state.cpyInternal();
  }

  public void CMP()
  {
    state.cmpInternal();
  }

  public void REP()
  {
    state.repInternal();
  }

  public void TAY()
  {
    state.tayInternal();
  }

  public void TAX()
  {
    state.taxInternal();
  }

  public void PHX()
  {
    state.setIndexData(state.getX(), false);
  }

  public void STP()
  {
    state.stopped = true;
  }

  public void DEC()
  {
    state.decInternal();
  }

  public void INY()
  {
    state.incrementY();
  }

  public void DEX()
  {
    state.decrementX();
  }

  public void BNE()
  {
    branch(!state.isZeroFlagSet());
  }

  public void CLD()
  {
    state.setDecimalFlag(false);
  }

  public void CPX()
  {
    state.cpxInternal();
  }

  public void SBC()
  {
    state.sbcInternal();
  }

  public void SEP()
  {
    state.sepInternal();
  }

  public void INC()
  {
    state.incInternal();
  }

  public void INX()
  {
    state.incrementX();
  }

  public void NOP()
  {
  }

  public void XBA()
  {
    state.xbaInternal();
  }

  public void BEQ()
  {
    branch(state.isZeroFlagSet());
  }

  public void SED()
  {
    state.setDecimalFlag(true);
  }

  public void PLX()
  {
    state.setX(state.getIndexData());
  }

  public void XCE()
  {
    state.xceInternal();
  }

  public void ABORT()
  {
    state.setInterruptDisableFlag(true);
    state.setDecimalFlag(false);
  }

  public void IRQ()
  {
    state.setInterruptDisableFlag(true);
    state.setDecimalFlag(false);
  }

  public void NMI()
  {
    state.setInterruptDisableFlag(true);
    state.setDecimalFlag(false);
  }

  public void RES()
  {
    state.resetInternal();
  }

  public String getAddressValueHex()
  {
    Address address = state.getAddress();
    return getByteStringHex(address.getBank()) + ":" + to16BitHex(address.getOffset());
  }

  public String getAccumulatorValueHex()
  {
    return getWordStringHex(state.getA());
  }

  public String getXValueHex()
  {
    return getWordStringHex(state.getX());
  }

  public String getYValueHex()
  {
    return getWordStringHex(state.getY());
  }

  public String getDataBankValueHex()
  {
    return getByteStringHex(state.getDataBank());
  }

  public String getStackValueHex()
  {
    return getWordStringHex(state.getStackPointer());
  }

  public String getDirectPageValueHex()
  {
    return getWordStringHex(state.getDirectPage());
  }

  public String getProgramCounterValueHex()
  {
    return getByteStringHex(state.getProgramCounter().getBank()) + ":" + to16BitHex(state.getProgramCounter().getOffset());
  }

  public String getDataValueHex()
  {
    return getWordStringHex(state.getData16Bit());
  }

  public String getOpcodeValueHex()
  {
    return getOpcodeValueHex(state.getCycle(), state.getOpCode());
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
    return state.getOpCode().getName();
  }

  private String getStatusString()
  {
    String z = state.isZeroFlagSet() ? "Z:1 " : "Z:0 ";
    String n = state.isNegativeSet() ? "N:1 " : "N:0 ";
    String d = state.isDecimal() ? "D:1 " : "D:0 ";
    String i = state.isInterruptDisable() ? "I:1 " : "I:0 ";
    String m = state.isMemory8Bit() ? "M8  " : "M16 ";
    String x = "";
    boolean emulation = state.isEmulation();
    if (!emulation)
    {
      x = state.isIndex8Bit() ? "X8  " : "X16 ";
    }
    String c = state.isCarrySet() ? "C1 " : "C0 ";
    String e = emulation ? "E1 " : "E0 ";
    String o = state.isOverflowFlag() ? "O1 " : "O0 ";
    String b = "";
    if (emulation)
    {
      b = state.isBreak() ? "B1 " : "B0 ";
    }
    return z + n + d + i + m + x + c + e + o + b;
  }

  public String getType()
  {
    return TYPE;
  }

  public String getCycleString()
  {
    return Integer.toString(state.getCycle());
  }

  public int getData()
  {
    return state.getData();
  }

  public void setData(int data)
  {
    state.setData(data);
  }

  @Override
  public W65C816State createState()
  {
    return new W65C816State();
  }

  public boolean isIndex8Bit()
  {
    return state.isIndex8Bit();
  }

  public boolean isMemory8Bit()
  {
    return state.isMemory8Bit();
  }

  public boolean isMemory16Bit()
  {
    return state.isMemory16Bit();
  }
}

