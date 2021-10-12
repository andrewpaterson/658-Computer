package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.StackRelativeIndirectIndexedWithY;
import static net.wdc65xx.wdc65816.WidthFromRegister.M;

public class StackRelativeIndirectIndexedWithYWriteCycles
    extends InstructionCycles
{
  public StackRelativeIndirectIndexedWithYWriteCycles(Executor<Cpu65816> operation)
  {
    super(StackRelativeIndirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), SP_inc()),
          new BusCycle(Address(S(), D0()), Read_AAL(), SP_inc()),
          new BusCycle(Address(S(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(S(), D0(), o(1)), IO()),
          new BusCycle(Address(DBR(), AA(), Y()), E(operation), Write_DataLow(), DONE8Bit(M)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), Write_DataHigh(), DONE16Bit(M)));
  }
}

