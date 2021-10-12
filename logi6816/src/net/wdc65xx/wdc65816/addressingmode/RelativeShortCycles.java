package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.Relative;

public class RelativeShortCycles
    extends InstructionCycles
{
  //20
  public RelativeShortCycles(Executor<Cpu65816> operation)
  {
    super(Relative,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_DataLow(), PC_inc(), E(operation)),  //Done if branch not taken
          new BusCycle(Address(PBR(), PC()), IO(), new NoteSix()),
          new BusCycle(Address(PBR(), PC()), PC_e(PBR(), PC(), new SignedDataLow()), IO(), DONE()));
  }
}

