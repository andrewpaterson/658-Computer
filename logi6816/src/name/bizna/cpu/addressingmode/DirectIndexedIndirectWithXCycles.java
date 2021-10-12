package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import static name.bizna.cpu.AddressingMode.DirectIndexedIndirectWithX;
import static name.bizna.cpu.WidthFromRegister.M;

public class DirectIndexedIndirectWithXCycles
    extends InstructionCycles
{
  //11
  public DirectIndexedIndirectWithXCycles(Executor<Cpu65816> operation)
  {
    super(DirectIndexedIndirectWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(DP(), D0(), X()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), X(), o(1)), Read_AAH()),
          new BusCycle(Address(DBR(), AA()), Read_DataLow(), E8Bit(operation, M), DONE8Bit(M)),
          new BusCycle(Address(DBR(), AA(), o(1)), Read_DataHigh(), E16Bit(operation, M), DONE16Bit(M)));
  }
}

