package net.logicim.data.passive.wire;

import net.logicim.common.type.Int2D;
import net.logicim.data.circuit.SubcircuitPinAlignment;
import net.logicim.data.circuit.SubcircuitPinAnchour;
import net.logicim.data.common.Radix;
import net.logicim.data.family.Family;
import net.logicim.data.integratedcircuit.common.PassiveData;
import net.logicim.data.port.common.SimulationMultiPortData;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.simulation.SubcircuitEditor;
import net.logicim.ui.simulation.component.passive.pin.PinPropertyHelper;
import net.logicim.ui.simulation.component.passive.pin.PinView;

import java.util.List;

public class PinData
    extends PassiveData<PinView>
{
  protected int bitWidth;
  protected Family family;
  protected boolean explicitPowerPorts;
  protected SubcircuitPinAlignment alignment;
  protected SubcircuitPinAnchour offset;
  protected int weight;
  protected boolean inverting;
  protected boolean clockNotch;
  protected Radix radix;

  public PinData()
  {
  }

  public PinData(Int2D position,
                 Rotation rotation,
                 String name,
                 List<SimulationMultiPortData> ports,
                 long id,
                 boolean selected,
                 int bitWidth,
                 SubcircuitPinAlignment alignment,
                 SubcircuitPinAnchour offset,
                 int weight,
                 boolean inverting,
                 boolean clockNotch,
                 Family family,
                 boolean explicitPowerPorts,
                 Radix radix)
  {
    super(position,
          rotation,
          name,
          ports,
          id,
          selected);
    this.bitWidth = bitWidth;
    this.alignment = alignment;
    this.offset = offset;
    this.weight = weight;
    this.inverting = inverting;
    this.clockNotch = clockNotch;
    this.family = family;
    this.explicitPowerPorts = explicitPowerPorts;
    this.radix = radix;
  }

  @Override
  protected PinView createComponentView(SubcircuitEditor subcircuitEditor, boolean newComponentPropertyStep)
  {
    PinProperties properties = new PinProperties(name,
                                                 bitWidth,
                                                 alignment,
                                                 offset,
                                                 weight,
                                                 inverting,
                                                 clockNotch,
                                                 FamilyStore.getInstance().get(family),
                                                 explicitPowerPorts,
                                                 radix);
    if (!newComponentPropertyStep)
    {
      PinPropertyHelper helper = new PinPropertyHelper(subcircuitEditor.getSubcircuitView().findAllPins());
      helper.ensureUniquePinName(properties);
      helper.ensureNextWeight(properties);
    }
    return new PinView(subcircuitEditor.getSubcircuitView(),
                       position,
                       rotation,
                       properties);
  }
}

