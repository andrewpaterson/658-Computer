package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.AbsoluteLongIndexedWithX;
import static net.wdc65xx.wdc65816.WidthFromRegister.M;

public class AbsoluteLongIndexedWithXWriteCycles
    extends InstructionCycles
{
  //5
  public AbsoluteLongIndexedWithXWriteCycles(Executor<Cpu65816> operation)
  {
    super(AbsoluteLongIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAB(), PC_inc()),
          new BusCycle(Address(AAB(), AA(), X()), E(operation), Write_DataLow(), DONE8Bit(M)),
          new BusCycle(Address(AAB(), AA(), X(), o(1)), Write_DataHigh(), DONE16Bit(M)));
  }
}

