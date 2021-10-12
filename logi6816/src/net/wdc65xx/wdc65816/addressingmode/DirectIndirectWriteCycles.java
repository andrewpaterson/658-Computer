package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.DirectIndirect;
import static net.wdc65xx.wdc65816.WidthFromRegister.M;

public class DirectIndirectWriteCycles
    extends InstructionCycles
{
  public DirectIndirectWriteCycles(Executor<Cpu65816> operation)
  {
    //12
    super(DirectIndirect,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DBR(), AA()), E(operation), Write_DataLow(), DONE8Bit(M)),
          new BusCycle(Address(DBR(), AA(), o(1)), Write_DataHigh(), DONE16Bit(M)));
  }
}

