package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.RelativeLong;

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

