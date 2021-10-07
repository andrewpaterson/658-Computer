package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.Relative;

public class RelativeShortCycles
    extends InstructionCycles
{
  //20
  public RelativeShortCycles(Consumer<Cpu65816> operation)
  {
    super(Relative,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_DataLow(), PC_inc(), E(operation)),  //Done if branch not taken
          new BusCycle(Address(PBR(), PC()), IO(), new NoteSix()),
          new BusCycle(Address(PBR(), PC()), PC_e(PBR(), PC(), new SignedDataLow()), IO(), DONE()));
  }
}

