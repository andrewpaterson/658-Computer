package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;
import name.bizna.cpu.WidthFromRegister;

import static name.bizna.cpu.AddressingMode.Absolute;

public class AbsoluteWriteCycles
    extends InstructionCycles
{
  //1a
  public AbsoluteWriteCycles(Executor<Cpu65816> operation, WidthFromRegister width)
  {
    super(Absolute,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(DBR(), AA()), E(operation), Write_DataLow(), DONE8Bit(width)),
          new BusCycle(Address(DBR(), AA(), o(1)), Write_DataHigh(), DONE16Bit(width)));
  }
}

