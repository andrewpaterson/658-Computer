package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.StackRelative;
import static net.wdc65xx.wdc65816.WidthFromRegister.M;

public class StackRelativeCycles
    extends InstructionCycles
{
  public StackRelativeCycles(Executor<Cpu65816> operation)
  {
    //23
    super(StackRelative,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), D0(), o(1)), SP_inc(), Read_DataLow(), E8Bit(operation, M), DONE8Bit(M)),
          new BusCycle(Address(S(), D0(), o(1)), SP_inc(), Read_DataHigh(), E16Bit(operation, M), DONE16Bit(M)));
  }
}

