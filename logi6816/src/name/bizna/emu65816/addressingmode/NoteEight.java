package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

import static name.bizna.emu65816.OpCodeName.REP_Immediate;
import static name.bizna.emu65816.OpCodeName.SEP_Immediate;

public class NoteEight
    extends Operation
{
  @Override
  public boolean mustExecute(Cpu65816 cpu)
  {
    int opCode = cpu.getOpCode().getCode();
    return opCode == SEP_Immediate || opCode == REP_Immediate;
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
  }
}

