package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.DirectIndirectLong;
import static net.wdc65xx.wdc65816.WidthFromRegister.M;

public class DirectIndirectLongWriteCycles
    extends InstructionCycles
{
  //15
  public DirectIndirectLongWriteCycles(Executor<Cpu65816> operation)
  {
    super(DirectIndirectLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DP(), D0(), o(2)), Read_AAB()),
          new BusCycle(Address(AAB(), AA()), E(operation), Write_DataLow(), DONE8Bit(M)),
          new BusCycle(Address(AAB(), AA(), o(1)), Write_DataHigh(), DONE16Bit(M)));
  }
}

