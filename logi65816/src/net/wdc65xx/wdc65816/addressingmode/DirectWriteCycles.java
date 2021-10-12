package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;
import net.wdc65xx.wdc65816.WidthFromRegister;

import static net.wdc65xx.wdc65816.AddressingMode.Direct;

public class DirectWriteCycles
    extends InstructionCycles
{
  //10a
  public DirectWriteCycles(Executor<Cpu65816> operation, WidthFromRegister width)
  {
    super(Direct,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), E(operation), Write_DataLow(), DONE8Bit(width)),
          new BusCycle(Address(DP(), D0(), o(1)), Write_DataHigh(), DONE16Bit(width)));
  }
}

