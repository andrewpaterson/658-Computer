package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;
import name.bizna.cpu.WidthFromRegister;

import static name.bizna.cpu.AddressingMode.AbsoluteLong;

public class AbsoluteLongWriteCycles
    extends InstructionCycles
{
  //4a
  public AbsoluteLongWriteCycles(Executor<Cpu65816> operation, WidthFromRegister width)
  {
    super(AbsoluteLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAB(), PC_inc()),
          new BusCycle(Address(AAB(), AA()), E(operation), Write_DataLow(), DONE8Bit(width)),
          new BusCycle(Address(AAB(), AA(), o(1)), Write_DataHigh(), DONE16Bit(width)));
  }
}

