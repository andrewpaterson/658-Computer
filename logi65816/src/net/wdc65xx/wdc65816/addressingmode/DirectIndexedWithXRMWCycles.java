package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.DirectIndexedWithX;
import static net.wdc65xx.wdc65816.WidthFromRegister.M;

public class DirectIndexedWithXRMWCycles
    extends InstructionCycles
{
  //16b
  public DirectIndexedWithXRMWCycles(Executor<Cpu65816> operation)
  {
    super(DirectIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(RMW), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(RMW), new NoteTwo()),
          new BusCycle(Address(PBR(), PC()), IO(RMW)),
          new BusCycle(Address(DP(), D0(), X()), Read_DataLow(RMW)),
          new BusCycle(Address(DP(), D0(), X(), o(1)), Read_DataHigh(RMW), new NoteOne(M)),
          new BusCycle(Address(DP(), D0(), X(), o(1)), IO(RMW), E(operation)),
          new BusCycle(Address(DP(), D0(), X(), o(1)), Write_DataHigh(RMW), new NoteOne(M)),
          new BusCycle(Address(DP(), D0(), X()), Write_DataLow(RMW), DONE()));
  }
}

