package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import static name.bizna.cpu.AddressingMode.DirectIndirectIndexedWithY;
import static name.bizna.cpu.WidthFromRegister.M;

public class DirectIndirectIndexedWithYCycles
    extends InstructionCycles
{
  //13
  public DirectIndirectIndexedWithYCycles(Executor<Cpu65816> operation)
  {
    super(DirectIndirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DBR(), AAH(), AAL_YL()), IO(), new NoteFourY(true)),
          new BusCycle(Address(DBR(), AA(), Y()), Read_DataLow(), E8Bit(operation, M), DONE8Bit(M)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), Read_DataHigh(), E16Bit(operation, M), DONE16Bit(M)));
  }
}

