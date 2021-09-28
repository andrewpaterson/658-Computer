package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

import static name.bizna.emu65816.OpCodeName.REP_Immediate;
import static name.bizna.emu65816.OpCodeName.SEP_Immediate;

public class ImmediateDataHigh
    extends DataBusCycleOperation
{
  public ImmediateDataHigh()
  {
    super(false, true, true, true, true);
  }

  @Override
  public boolean shouldSkipCycle(Cpu65816 cpu)
  {
    boolean sixteenBitRegisters = cpu.isAccumulator8Bit() && cpu.isIndex8Bit();
    OpCode opCode = cpu.getOpCode();
    boolean sepOrRep = opCode.getCode() == SEP_Immediate || opCode.getCode() == REP_Immediate;
    return !sixteenBitRegisters && !sepOrRep;
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setImmediateHigh(cpu.getPinData());
  }
}

