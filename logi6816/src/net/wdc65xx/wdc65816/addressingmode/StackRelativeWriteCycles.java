package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.StackRelative;
import static net.wdc65xx.wdc65816.WidthFromRegister.M;

public class StackRelativeWriteCycles
    extends InstructionCycles
{
  public StackRelativeWriteCycles(Executor<Cpu65816> operation)
  {
    super(StackRelative,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), SP_inc()),
          new BusCycle(Address(S(), D0()), SP_inc(), E(operation), Write_DataLow(), DONE8Bit(M)),
          new BusCycle(Address(S(), D0(), o(1)), Write_DataHigh(), DONE16Bit(M)));
  }
}

