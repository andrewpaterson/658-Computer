package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import static name.bizna.cpu.AddressingMode.StackRelativeIndirectIndexedWithY;
import static name.bizna.cpu.WidthFromRegister.M;

public class StackRelativeIndirectIndexedWithYCycles
    extends InstructionCycles
{
  public StackRelativeIndirectIndexedWithYCycles(Executor<Cpu65816> operation)
  {
    //24
    super(StackRelativeIndirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), D0(), o(1)), Read_AAL(), SP_inc()),
          new BusCycle(Address(S(), D0(), o(1)), Read_AAH(), SP_inc()),
          new BusCycle(Address(S(), D0(), o(1)), IO()),
          new BusCycle(Address(DBR(), AA(), Y()), Read_DataLow(), E8Bit(operation, M), DONE8Bit(M)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), Read_DataHigh(), E16Bit(operation, M), DONE16Bit(M)));
  }
}

