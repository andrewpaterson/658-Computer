package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.RelativeLong;

public class RelativeLongCycles
    extends InstructionCycles
{
  //21
  public RelativeLongCycles(Executor<Cpu65816> operation)
  {
    super(RelativeLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_DataLow(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_DataHigh(), PC_inc(), E(operation)),  //Done if branch not taken
          new BusCycle(Address(PBR(), PC()), PC_e(PBR(), PC(), new SignedData()), IO(), DONE()));
  }
}

