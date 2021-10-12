package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;
import name.bizna.cpu.WidthFromRegister;

import static name.bizna.cpu.AddressingMode.Immediate;

public class ImmediateCycles
    extends InstructionCycles
{
  //18
  public ImmediateCycles(Executor<Cpu65816> operation, WidthFromRegister width)
  {
    super(Immediate,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), PC_inc(), Read_DataLow(), E8Bit(operation, width), DONE8Bit(width)),
          new BusCycle(Address(PBR(), PC()), PC_inc(), Read_DataHigh(), E16Bit(operation, width), DONE16Bit(width)));
  }
}

