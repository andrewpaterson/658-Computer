package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import static name.bizna.cpu.AddressingMode.AbsoluteLongIndexedWithX;
import static name.bizna.cpu.WidthFromRegister.M;

public class AbsoluteLongIndexedWithXCycles
    extends InstructionCycles
{
  //5
  public AbsoluteLongIndexedWithXCycles(Executor<Cpu65816> operation)
  {
    super(AbsoluteLongIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAB(), PC_inc()),
          new BusCycle(Address(AAB(), AA(), X()), Read_DataLow(), E8Bit(operation), DONE8Bit(M)),
          new BusCycle(Address(AAB(), AA(), X(), o(1)), Read_DataHigh(), E16Bit(operation), DONE16Bit(M)));
  }
}

