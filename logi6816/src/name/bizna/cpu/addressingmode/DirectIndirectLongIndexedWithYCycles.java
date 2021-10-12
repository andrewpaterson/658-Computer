package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import static name.bizna.cpu.AddressingMode.DirectIndirectLongIndexedWithY;
import static name.bizna.cpu.WidthFromRegister.M;

public class DirectIndirectLongIndexedWithYCycles
    extends InstructionCycles
{
  //14
  public DirectIndirectLongIndexedWithYCycles(Executor<Cpu65816> operation)
  {
    super(DirectIndirectLongIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DP(), D0(), o(2)), Read_AAB()),
          new BusCycle(Address(AAB(), AA(), Y()), Read_DataLow(), E8Bit(operation), DONE8Bit(M)),
          new BusCycle(Address(AAB(), AA(), Y(), o(1)), Read_DataHigh(), E16Bit(operation), DONE16Bit(M)));
  }
}

