package net.logicim.ui.circuit;

import net.common.SimulatorException;
import net.common.geometry.Line;
import net.common.geometry.LineMinimiser;
import net.common.geometry.LinePositionCache;
import net.common.geometry.LineSplitter;
import net.common.type.Float2D;
import net.common.type.Int2D;
import net.common.type.Positions;
import net.logicim.data.circuit.SubcircuitData;
import net.logicim.data.circuit.SubcircuitEditorData;
import net.logicim.data.integratedcircuit.common.StaticData;
import net.logicim.data.subciruit.SubcircuitInstanceData;
import net.logicim.data.wire.TraceData;
import net.logicim.domain.CircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitInstance;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulations;
import net.logicim.domain.passive.subcircuit.SubcircuitTopSimulation;
import net.logicim.ui.circuit.path.UpdatedViewPaths;
import net.logicim.ui.circuit.path.ViewPath;
import net.logicim.ui.circuit.path.ViewPathCircuitSimulation;
import net.logicim.ui.circuit.path.ViewPaths;
import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.LineOverlap;
import net.logicim.ui.common.TraceOverlap;
import net.logicim.ui.common.integratedcircuit.*;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.common.wire.TraceViewConnectionViewSetsPair;
import net.logicim.ui.common.wire.TunnelView;
import net.logicim.ui.common.wire.WireView;
import net.logicim.ui.connection.*;
import net.logicim.ui.shape.common.BoundingBox;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.ConnectionViewCache;
import net.logicim.ui.simulation.StaticViewIterator;
import net.logicim.ui.simulation.component.decorative.common.DecorativeView;
import net.logicim.ui.simulation.component.passive.pin.PinView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceCreation;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.component.subcircuit.ViewPathComponents;

import java.util.*;

import static net.logicim.ui.common.LineOverlap.getOverlap;

public class SubcircuitView
{
  protected String typeName;  //The subcircuit instance name is on the SubcircuitInstanceView.

  protected CircuitEditor circuitEditor;
  protected CircuitInstanceView subcircuitEditor;

  protected Set<IntegratedCircuitView<?, ?>> integratedCircuitViews;
  protected Set<PassiveView<?, ?>> passiveViews;
  protected Set<DecorativeView<?>> decorativeViews;
  protected Set<SubcircuitInstanceView> subcircuitInstanceViews;

  protected Set<TraceView> traceViews;
  protected Set<TunnelView> tunnelViews;
  protected Map<String, Set<TunnelView>> tunnelViewsMap;

  protected SubcircuitSimulations simulations;

  protected ConnectionViewCache connectionViewCache;
  protected List<ViewPath> viewPaths;

  public SubcircuitView(CircuitEditor circuitEditor)
  {
    this.circuitEditor = circuitEditor;
    this.subcircuitEditor = null;

    this.integratedCircuitViews = new LinkedHashSet<>();
    this.traceViews = new LinkedHashSet<>();
    this.tunnelViewsMap = new LinkedHashMap<>();
    this.passiveViews = new LinkedHashSet<>();
    this.subcircuitInstanceViews = new LinkedHashSet<>();
    this.tunnelViews = new LinkedHashSet<>();
    this.decorativeViews = new LinkedHashSet<>();
    this.connectionViewCache = new ConnectionViewCache();
    this.simulations = new SubcircuitSimulations();
    this.viewPaths = new ArrayList<>();
  }

  public static List<Line> getTraceViewLines(Collection<TraceView> traceViews)
  {
    List<Line> newLines = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      newLines.add(traceView.getLine());
    }
    return newLines;
  }

  public List<View> getAllViews()
  {
    List<View> views = new ArrayList<>();
    views.addAll(traceViews);
    views.addAll(tunnelViews);
    views.addAll(decorativeViews);
    views.addAll(passiveViews);
    views.addAll(integratedCircuitViews);
    views.addAll(subcircuitInstanceViews);
    return views;
  }

  public List<StaticView<?>> getStaticViews()
  {
    List<StaticView<?>> views = new ArrayList<>();
    views.addAll(tunnelViews);
    views.addAll(decorativeViews);
    views.addAll(passiveViews);
    views.addAll(integratedCircuitViews);
    views.addAll(subcircuitInstanceViews);
    return views;
  }

  public List<ComponentView<?>> getComponentViews()
  {
    List<ComponentView<?>> views = new ArrayList<>();
    views.addAll(passiveViews);
    views.addAll(integratedCircuitViews);
    return views;
  }

  public List<SubcircuitInstanceView> getSubcircuitInstanceViews()
  {
    return new ArrayList<>(subcircuitInstanceViews);
  }

  public List<SubcircuitInstanceView> getSubcircuitInstanceViews(SubcircuitView instanceSubcircuitView)
  {
    ArrayList<SubcircuitInstanceView> result = new ArrayList<>();
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      if (subcircuitInstanceView.getInstanceSubcircuitView() == instanceSubcircuitView)
      {
        result.add(subcircuitInstanceView);
      }
    }
    return result;
  }

  public List<ConnectionView> disconnectTraceViewAndDestroyComponents(TraceView traceView)
  {
    List<ConnectionView> connectionViews = traceView.getConnectionViews();
    connectionViewCache.removeAll(viewPaths, traceView, connectionViews);
    traceView.disconnectViewAndDestroyComponents();
    return connectionViews;
  }

  public List<ConnectionView> disconnectStaticViewAndDestroyComponents(StaticView<?> staticView)
  {
    if (staticView == null)
    {
      throw new SimulatorException("SubcircuitView [%s] cannot disconnect [null] view.", getTypeName());
    }

    List<ConnectionView> connectionViews = staticView.getConnectionViews();
    if (connectionViews == null)
    {
      throw new SimulatorException("SubcircuitView [%s] cannot disconnect %s with [null] connections.", getTypeName(), staticView.toIdentifierString());
    }

    connectionViewCache.removeAll(viewPaths, staticView, connectionViews);
    staticView.disconnectViewAndDestroyComponents();

    return connectionViews;
  }

  public void deleteStaticView(StaticView<?> staticView)
  {
    List<StaticView<?>> staticViews = new ArrayList<>();
    staticViews.add(staticView);
    deleteStaticViews(staticViews);
  }

  public void deleteStaticViews(List<StaticView<?>> staticViews)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (StaticView<?> componentView : staticViews)
    {
      if (componentView == null)
      {
        throw new SimulatorException("SubcircuitView [%s] cannot delete a [null] view.", getTypeName());
      }

      List<ConnectionView> disconnectedConnectionViews = disconnectStaticViewAndDestroyComponents(componentView);
      for (ConnectionView connectionView : disconnectedConnectionViews)
      {
        if (connectionView == null)
        {
          throw new SimulatorException("SubcircuitView [%s] disconnected Connection View may not be [null] from component [%s].", getTypeName(), componentView.toIdentifierString());
        }
      }

      connectionViews.addAll(disconnectedConnectionViews);
      if (componentView instanceof IntegratedCircuitView)
      {
        removeIntegratedCircuitView((IntegratedCircuitView<?, ?>) componentView);
      }
      else if (componentView instanceof PassiveView)
      {
        removePassiveView((PassiveView<?, ?>) componentView);
      }
      else if (componentView instanceof SubcircuitInstanceView)
      {
        SubcircuitInstanceView subcircuitInstanceView = (SubcircuitInstanceView) componentView;
        SubcircuitView subcircuitView = subcircuitInstanceView.getInstanceSubcircuitView();
        removeSubcircuitInstanceView(subcircuitInstanceView);

        UpdatedViewPaths updatedPaths = getCircuitEditor().viewPathsUpdate();
        subcircuitView.pathsUpdated(updatedPaths);
      }
      else if (componentView instanceof DecorativeView)
      {
        removeDecorativeView((DecorativeView<?>) componentView);
      }
      else if (componentView instanceof TunnelView)
      {
        removeTunnelView((TunnelView) componentView);
      }
      else
      {
        throw new SimulatorException("Cannot delete view of class [%s].", componentView.getClass().getSimpleName());
      }
    }

    Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViews(getCircuitEditor().getViewPaths().getEmptyPath(),
                                                                                subcircuitEditor,
                                                                                connectionViews);
    fireConnectionEvents(updatedConnectionViews);
  }

  public void pathsUpdated(UpdatedViewPaths updatedPaths)
  {
    List<ViewPath> newPaths = new ArrayList<>();
    List<ViewPath> removedPaths = new ArrayList<>();

    for (ViewPath newPath : updatedPaths.getNewPaths())
    {
      if (newPath.endsWithSubcircuitView(this))
      {
        viewPaths.add(newPath);
        newPaths.add(newPath);
      }
    }

    for (ViewPath removedPath : updatedPaths.getRemovedPaths())
    {
      boolean removed = viewPaths.remove(removedPath);
      if (removed)
      {
        removedPaths.add(removedPath);
      }
    }

    connectionViewCache.addPaths(newPaths);
    connectionViewCache.removePaths(removedPaths);
  }

  public void fireConnectionEvents(Set<ConnectionView> connectionViews)
  {
    Set<PortView> portViews = getPortViews(connectionViews);
    for (PortView portView : portViews)
    {
      portView.traceConnected();
    }
  }

  public void deleteTraceViews(Set<TraceView> inputTraceViews)
  {
    ConnectionViewNetFinder finder = new ConnectionViewNetFinder();
    for (TraceView traceView : inputTraceViews)
    {
      List<TraceOverlap> tracesTouching = getTracesTouching(traceView.getLine());
      for (TraceOverlap traceOverlap : tracesTouching)
      {
        TraceView touchingTraceView = traceOverlap.getTraceView();
        if (!inputTraceViews.contains(touchingTraceView))
        {
          finder.addConnectionToProcess(touchingTraceView.getStartConnection());
          finder.addConnectionToProcess(touchingTraceView.getEndConnection());
        }
      }
    }

    Set<ConnectionView> nonTraceViewConnectionViews = findNonTraceViewConnectionViews(inputTraceViews);
    removeTraceViews(inputTraceViews);

    finder.process();

    Set<TraceView> connectedTraceViews = finder.getTraceViews();
    Set<TraceView> newTraceViews = createTraceViews(new HashSet<>(), connectedTraceViews);
    createTracesForTraceViewsAndConnectionViews(getCircuitEditor().getViewPaths().getEmptyPath(),
                                                subcircuitEditor,
                                                nonTraceViewConnectionViews,
                                                newTraceViews);
  }

  public ConnectionView getOrAddConnectionView(Int2D position, View view)
  {
    return connectionViewCache.getOrAddConnectionView(viewPaths, position, view);
  }

  public StaticViewIterator staticViewIterator()
  {
    return new StaticViewIterator(tunnelViews,
                                  integratedCircuitViews,
                                  passiveViews,
                                  subcircuitInstanceViews,
                                  decorativeViews);
  }

  public Set<TraceView> getTraceViews()
  {
    return traceViews;
  }

  public void validate()
  {
    validateConnectionViews();
    validateStaticViews();
    validateTracesContainOnlyCurrentViews();
    validateTunnelViews();

    validateComponentSimulations();
  }

  private void validateStaticViews()
  {
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      staticView.validate();
    }
  }

  protected void validateComponentSimulations()
  {
    validateComponentSimulations(subcircuitInstanceViews, "subcircuit instance view");
    validateComponentSimulations(integratedCircuitViews, "integrated circuit view");
    validateComponentSimulations(passiveViews, "passive view");

    validateWireSimulations(traceViews, "trace view");
    validateWireSimulations(tunnelViews, "tunnel view");
  }

  protected void validateComponentSimulations(Set<? extends ComponentView<?>> componentViews, String type)
  {
    for (ComponentView<?> componentView : componentViews)
    {
      if (componentView.isEnabled())
      {
        Set<SubcircuitSimulation> simulationSubcircuitSimulations = new HashSet<>(simulations.getSubcircuitSimulations());
        Set<? extends SubcircuitSimulation> componentSubcircuitSimulations = componentView.getComponentSubcircuitSimulations();
        if (!simulationSubcircuitSimulations.containsAll(componentSubcircuitSimulations))
        {
          throw new SimulatorException("Subcircuit view [%s] simulations (of count [%s]) do not contain all %s [%s] simulations (of count [%s]).",
                                       getTypeName(),
                                       simulationSubcircuitSimulations.size(),
                                       type,
                                       componentView.getDescription(),
                                       componentSubcircuitSimulations);
        }

        validatePortSimulations(componentView.getPortViews());
      }
    }
  }

  protected void validatePortSimulations(List<PortView> portViews)
  {
    for (PortView portView : portViews)
    {
      Set<? extends SubcircuitSimulation> simulationSubcircuitSimulations = new HashSet<>(simulations.getSubcircuitSimulations());
      Set<? extends SubcircuitSimulation> componentSubcircuitSimulations = portView.getPortSubcircuitSimulations();
      if (!simulationSubcircuitSimulations.containsAll(componentSubcircuitSimulations))
      {
        throw new SimulatorException("Subcircuit view [%s] simulations (of count [%s]) do not contain all Port View [%s] simulations (of count [%s]).",
                                     getTypeName(),
                                     simulationSubcircuitSimulations.size(),
                                     portView.getDescription(),
                                     componentSubcircuitSimulations.size());
      }
    }
  }

  protected void validateWireSimulations(Set<? extends WireView> wireViews, String type)
  {
    for (WireView wireView : wireViews)
    {
      Set<? extends SubcircuitSimulation> simulationSubcircuitSimulations = new HashSet<>(simulations.getSubcircuitSimulations());
      Set<? extends SubcircuitSimulation> componentSubcircuitSimulations = wireView.getWireSubcircuitSimulations();
      if (!simulationSubcircuitSimulations.containsAll(componentSubcircuitSimulations))
      {
        throw new SimulatorException("Subcircuit view [%s] simulations (of count [%s]) do not contain all %s [%s] simulations (of count [%s]).",
                                     getTypeName(),
                                     simulationSubcircuitSimulations.size(),
                                     type,
                                     wireView.getDescription(),
                                     componentSubcircuitSimulations);
      }
    }
  }

  private void validateConnectionViews()
  {
    StaticViewIterator iterator = staticViewIterator();
    Set<ConnectionView> viewConnectionViews = new LinkedHashSet<>();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      if (staticView instanceof ComponentView)
      {
        ComponentView<?> componentView = (ComponentView<?>) staticView;
        List<PortView> portViews = componentView.getPortViews();
        for (PortView portView : portViews)
        {
          Int2D portPosition = portView.getGridPosition();
          ConnectionView connectionView = portView.getConnection();
          if (connectionView != null)
          {
            Int2D connectionPosition = connectionView.getGridPosition();
            if (connectionPosition == null)
            {
              throw new SimulatorException("Position on connection on %s cannot be null.", componentView.toIdentifierString());
            }

            if (!portPosition.equals(connectionPosition))
            {
              throw new SimulatorException("%s Port View [%s] position (%s) must be equal to connection position (%s).",
                                           componentView.toIdentifierString(),
                                           portView.getText(),
                                           Int2D.toString(portPosition),
                                           Int2D.toString(connectionPosition));
            }

            if (!connectionView.getConnectedComponents().contains(staticView))
            {
              throw new SimulatorException("Connection View [%s] does not contain %s.",
                                           connectionView.toString(),
                                           componentView.toIdentifierString());
            }

          }
          else
          {
            throw new SimulatorException("%s Port View [%s] may not have a null connection.",
                                         componentView.toIdentifierString(),
                                         portView.getText());
          }
        }
      }

      List<ConnectionView> localConnectionViews = staticView.getConnectionViews();
      viewConnectionViews.addAll(localConnectionViews);

      for (ConnectionView connectionView : localConnectionViews)
      {
        if (connectionView == null)
        {
          throw new SimulatorException("Connection on [%s] cannot be null.", staticView.toIdentifierString());
        }
        Int2D connectionPosition = connectionView.getGridPosition();
        if (connectionPosition == null)
        {
          throw new SimulatorException("Position on connection on [%s] cannot be null.", staticView.toIdentifierString());
        }

        ConnectionView cacheConnectionView = connectionViewCache.getConnectionView(connectionPosition);
        if (cacheConnectionView != connectionView)
        {
          throw new SimulatorException("[%s] connection (%s) must be equal to cache connection (%s).",
                                       staticView.toIdentifierString(),
                                       Int2D.toString(connectionPosition),
                                       ConnectionView.toPositionString(cacheConnectionView));
        }
      }
    }

    connectionViewCache.validatePositions();

    Set<ConnectionView> cacheConnectionViews = new LinkedHashSet<>(connectionViewCache.findAll());
    for (ConnectionView cacheConnectionView : cacheConnectionViews)
    {
      cacheConnectionView.validateContainingSubcircuitViews(this);

      List<View> connectedViews = cacheConnectionView.getConnectedComponents();
      if (connectedViews.size() == 0)
      {
        throw new SimulatorException("Cached Connection View [%s] must be connected to at least one View.", cacheConnectionView.toString());
      }

      for (View componentView : connectedViews)
      {
        if (!componentView.getConnectionViews().contains(cacheConnectionView))
        {
          throw new SimulatorException("%s does not contain Connection View [%s].",
                                       componentView.toIdentifierString(),
                                       cacheConnectionView.toString());
        }
      }
    }

    for (TraceView traceView : traceViews)
    {
      if (traceView.isEnabled())
      {
        ConnectionView startConnection = traceView.getStartConnection();
        ConnectionView endConnection = traceView.getEndConnection();

        if (startConnection == null)
        {
          throw new SimulatorException("Start Connection on [%s] cannot be null.", traceView.toIdentifierString());
        }
        if (endConnection == null)
        {
          throw new SimulatorException("End Connection on [%s] cannot be null.", traceView.toIdentifierString());
        }

        viewConnectionViews.add(startConnection);
        viewConnectionViews.add(endConnection);
      }
    }

    List<ConnectionView> viewConnectionViewsList = new ArrayList<>(viewConnectionViews);
    List<ConnectionView> cacheConnectionViewsList = new ArrayList<>(cacheConnectionViews);

    Collections.sort(viewConnectionViewsList);
    Collections.sort(cacheConnectionViewsList);

    if (viewConnectionViews.size() != cacheConnectionViews.size())
    {
      throw new SimulatorException("SubcircuitView [%s] has cached connection views size [%s] but View connection views size [%s].",
                                   getTypeName(),
                                   cacheConnectionViewsList.size(),
                                   viewConnectionViewsList.size());
    }

    for (int i = 0; i < viewConnectionViewsList.size(); i++)
    {
      ConnectionView viewConnectionView = viewConnectionViewsList.get(i);
      ConnectionView cacheConnectionView = cacheConnectionViewsList.get(i);

      if (viewConnectionView != cacheConnectionView)
      {
        throw new SimulatorException("SubcircuitView [%s] has cached connection view [%s] at index [%s] does not match view connection view [%s].",
                                     getTypeName(),
                                     cacheConnectionView.toString(),
                                     i,
                                     viewConnectionView.toString());
      }
    }

    connectionViewCache.validatePathConnections(viewPaths);
  }

  protected void validateTunnelViews()
  {
    for (TunnelView tunnelView : tunnelViews)
    {
      if (!tunnelView.isRemoved())
      {
        String name = tunnelView.getName();
        String sanitisedName = name.trim().toLowerCase();
        if (!tunnelView.getSanitisedName().equals(sanitisedName))
        {
          throw new SimulatorException("SubcircuitView [%s] Tunnel view sanitised name is [%s] but should be [%s].", getTypeName(), tunnelView.getSanitisedName(), sanitisedName);
        }

        Set<TunnelView> tunnelViews = tunnelViewsMap.get(sanitisedName);
        if (!sanitisedName.isEmpty())
        {
          if (tunnelViews == null)
          {
            throw new SimulatorException("SubcircuitView [%s] TunnelViewsMap did not contain a map for name [%s]", getTypeName(), tunnelView.getName());
          }
          if (!tunnelViews.contains(tunnelView))
          {
            throw new SimulatorException("SubcircuitView [%s] TunnelViewsMap did not contain tunnel view [%s].", getTypeName(), tunnelView.getName());
          }
        }
      }
    }

    for (String sanitisedName : tunnelViewsMap.keySet())
    {
      Set<TunnelView> tunnelViews = tunnelViewsMap.get(sanitisedName);
      for (TunnelView tunnelView : tunnelViews)
      {
        if (!this.tunnelViews.contains(tunnelView))
        {
          throw new SimulatorException("SubcircuitView [%s] TunnelViews did not contain tunnel view [%s].", getTypeName(), tunnelView.getName());
        }
      }
    }
  }

  protected void validateTracesContainOnlyCurrentViews()
  {
    for (TraceView traceView : traceViews)
    {
      if (traceView.hasConnections())
      {
        ConnectionView startConnection = traceView.getStartConnection();
        for (View view : startConnection.getConnectedComponents())
        {
          boolean contained = true;
          if (view instanceof IntegratedCircuitView)
          {
            if (!integratedCircuitViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof TraceView)
          {
            if (!traceViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof PassiveView)
          {
            if (!passiveViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof SubcircuitInstanceView)
          {
            if (!subcircuitInstanceViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof DecorativeView)
          {
            if (!decorativeViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view instanceof TunnelView)
          {
            if (!tunnelViews.contains(view))
            {
              contained = false;
            }
          }
          else if (view == null)
          {
            throw new SimulatorException("SubcircuitView [%s] with TraceView [" + traceView.getDescription() + "] does not include trace has [null] connection.", getTypeName());
          }
          else
          {
            throw new SimulatorException("SubcircuitView [%s] with %s referenced by TraceView [%s] has not been included in validateConsistency.", getTypeName(), view.getDescription(), traceView.getDescription());
          }

          if (!contained)
          {
            throw new SimulatorException("SubcircuitView [%s] with %s referenced by TraceView [%s] does not include trace.", getTypeName(), view.getDescription(), traceView.getDescription());
          }
        }
      }
    }
  }

  public void addIntegratedCircuitView(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    synchronized (this)
    {
      integratedCircuitViews.add(integratedCircuitView);
    }
  }

  public void addPassiveView(PassiveView<?, ?> view)
  {
    synchronized (this)
    {
      passiveViews.add(view);
    }
  }

  public void addSubcircuitInstanceView(SubcircuitInstanceView view)
  {
    synchronized (this)
    {
      subcircuitInstanceViews.add(view);
    }
  }

  public void addDecorativeView(DecorativeView<?> view)
  {
    synchronized (this)
    {
      decorativeViews.add(view);
    }
  }

  protected void removeIntegratedCircuitView(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    synchronized (this)
    {
      integratedCircuitViews.remove(integratedCircuitView);
    }
  }

  protected boolean removePassiveView(PassiveView<?, ?> passiveView)
  {
    synchronized (this)
    {
      return passiveViews.remove(passiveView);
    }
  }

  protected boolean removeSubcircuitInstanceView(SubcircuitInstanceView subcircuitInstanceView)
  {
    synchronized (this)
    {
      return subcircuitInstanceViews.remove(subcircuitInstanceView);
    }
  }

  protected boolean removeDecorativeView(DecorativeView<?> decorativeView)
  {
    synchronized (this)
    {
      return decorativeViews.remove(decorativeView);
    }
  }

  protected void removeTraceView(TraceView traceView)
  {
    synchronized (this)
    {
      if (!traceView.hasConnections())
      {
        if (!traceViews.remove(traceView))
        {
          throw new SimulatorException("SubcircuitView [%s] cannot remove trace not in trace views.", getTypeName());
        }
      }
      else
      {
        throw new SimulatorException("SubcircuitView [%s] cannot remove trace view with connections.", getTypeName());
      }
    }
  }

  public void addTraceView(TraceView traceView)
  {
    if (!traceView.hasConnections())
    {
      throw new SimulatorException("SubcircuitView [%s] cannot add trace [%s] without connections.", getTypeName(), traceView.toIdentifierString());
    }

    synchronized (this)
    {
      traceViews.add(traceView);
    }
  }

  protected boolean removeTunnelView(TunnelView tunnelView)
  {
    synchronized (this)
    {
      String name = tunnelView.getSanitisedName();
      boolean removed = this.tunnelViews.remove(tunnelView);
      if (!name.isEmpty())
      {
        Set<TunnelView> tunnelViews = this.tunnelViewsMap.get(name);
        tunnelViews.remove(tunnelView);
        if (tunnelViews.isEmpty())
        {
          this.tunnelViewsMap.remove(name);
        }
      }
      return removed;
    }
  }

  public Set<TunnelView> addTunnel(TunnelView tunnelView)
  {
    synchronized (this)
    {
      this.tunnelViews.add(tunnelView);

      String name = tunnelView.getSanitisedName();
      if (!name.isEmpty())
      {
        Set<TunnelView> tunnelViews = this.tunnelViewsMap.get(name);
        if (tunnelViews == null)
        {
          tunnelViews = new LinkedHashSet<>();
          this.tunnelViewsMap.put(name, tunnelViews);
        }
        tunnelViews.add(tunnelView);
        return tunnelViews;
      }

      return null;
    }
  }

  public ConnectionView getConnection(int x, int y)
  {
    return connectionViewCache.getConnectionView(x, y);
  }

  public Set<ConnectionView> createTracesForConnectionViews(ViewPath parentViewPath,
                                                            CircuitInstanceView circuitInstanceView,
                                                            Collection<ConnectionView> traceConnectionViews)
  {
    Set<ConnectionView> allUpdatedConnectionViews = new LinkedHashSet<>();
    if (traceConnectionViews.size() > 0)
    {
      validateConnectionViewsNotNull(traceConnectionViews);
      Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViewsForSubcircuitSimulation(parentViewPath,
                                                                                                         circuitInstanceView,
                                                                                                         traceConnectionViews);
      allUpdatedConnectionViews.addAll(updatedConnectionViews);
    }

    // allUpdatedConnectionViews are returned for fireConnectionEvents.  Simulations that were already connected (is that possible?) should not fireConnectionEvents.  You should validate this on firing.
    return allUpdatedConnectionViews;
  }

  protected void validateConnectionViewsNotNull(Collection<ConnectionView> tracesConnectionViews)
  {
    for (ConnectionView connectionView : tracesConnectionViews)
    {
      if (connectionView == null)
      {
        throw new SimulatorException("SubcircuitView [%s] Connection View may not be [null].", getTypeName());
      }
    }
  }

  protected Set<ConnectionView> createTracesForConnectionViewsForSubcircuitSimulation(ViewPath parentViewPath,
                                                                                      CircuitInstanceView circuitInstanceView,
                                                                                      Collection<ConnectionView> tracesConnectionViews)
  {
    ViewPaths viewPaths = getCircuitEditor().getViewPaths();
    ViewPath viewPath = viewPaths.getViewPath(parentViewPath, circuitInstanceView);

    Set<ConnectionView> updatedConnectionViews = new LinkedHashSet<>();
    for (ConnectionView connectionView : tracesConnectionViews)
    {
      if (!updatedConnectionViews.contains(connectionView))
      {
        if (connectionView.getConnectedComponents().size() > 0)
        {
          WireListFinder wireListFinder = new WireListFinder(viewPath, connectionView, viewPaths);
          List<LocalMultiSimulationConnectionNet> connectionNets = wireListFinder.createConnectionNets();
          WireList wireList = wireListFinder.createWireList(connectionNets);

          for (SubcircuitSimulation subcircuitSimulation : simulations.getSubcircuitSimulations())
          {
            WireTraceConverter wireTraceConverter = new WireTraceConverter(wireList, subcircuitSimulation);
            wireTraceConverter.process();
          }

          List<ConnectionView> connectionNetConnectionViews = wireList.getConnectionViews();
          updatedConnectionViews.addAll(connectionNetConnectionViews);
        }
      }
    }
    return updatedConnectionViews;
  }

  public void removeTraceViews(Collection<TraceView> traceViews)
  {
    for (TraceView traceView : traceViews)
    {
      traceView.disable();
      disconnectTraceViewAndDestroyComponents(traceView);
    }

    for (TraceView traceView : traceViews)
    {
      removeTraceView(traceView);
    }
  }

  public List<ConnectionView> getOrCreateStaticViewConnections(List<StaticView<?>> staticViews)
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      connectionViews.addAll(staticView.getOrCreateConnectionViews());
    }
    return connectionViews;
  }

  public Set<ConnectionView> getConnectionViews(List<StaticView<?>> staticViews,
                                                Set<TraceView> traceViews)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (StaticView<?> staticView : staticViews)
    {
      connectionViews.addAll(staticView.getConnectionViews());
    }
    for (TraceView traceView : traceViews)
    {
      connectionViews.addAll(traceView.getConnectionViews());
    }
    return connectionViews;
  }

  public void enableViews(Collection<? extends View> views)
  {
    for (View view : views)
    {
      if (!view.isEnabled())
      {
        view.enable();
      }
      else
      {
        throw new SimulatorException("SubcircuitView [%s] View [%s] is already enabled.", getTypeName());
      }
    }
  }

  public Set<PortView> getPortViews(Set<ConnectionView> connectionViews)
  {
    Set<PortView> portViews = new HashSet<>();
    for (ConnectionView connectionView : connectionViews)
    {
      List<View> connectedComponents = connectionView.getConnectedComponents();
      for (View view : connectedComponents)
      {
        if (view instanceof ComponentView)
        {
          ComponentView<?> componentView = (ComponentView<?>) view;
          PortView portView = componentView.getPortView(connectionView);
          portViews.add(portView);
        }
      }
    }
    return portViews;
  }

  public List<TraceView> getTraceViewsInGridSpace(int x, int y)
  {
    List<TraceView> traceViews = new ArrayList<>();
    for (TraceView traceView : getTraceViews())
    {
      if (traceView.contains(x, y))
      {
        traceViews.add(traceView);
      }
    }
    return traceViews;
  }

  public List<TraceOverlap> getTracesTouching(Line line)
  {
    return getTracesTouching(line, traceViews);
  }

  private List<TraceOverlap> getTracesTouching(Line line, Collection<TraceView> traceViews)
  {
    List<TraceOverlap> overlaps = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      LineOverlap overlap = traceView.touches(line);
      if (overlap != LineOverlap.None)
      {
        overlaps.add(new TraceOverlap(overlap, traceView));
      }
    }
    return overlaps;
  }

  public Set<TraceView> createTraceViews(Collection<Line> traceLineViews)
  {
    ConnectionViewNetFinder finder = new ConnectionViewNetFinder();
    for (Line line : traceLineViews)
    {
      List<TraceOverlap> tracesTouching = getTracesTouching(line);
      for (TraceOverlap traceOverlap : tracesTouching)
      {
        TraceView traceView = traceOverlap.getTraceView();
        finder.addConnectionToProcess(traceView.getStartConnection());
        finder.addConnectionToProcess(traceView.getEndConnection());
      }
    }

    finder.process();

    Set<TraceView> traceViews = finder.getTraceViews();
    if ((traceViews.size() > 0) ||
        (traceLineViews.size() > 0))
    {
      return createTraceViews(traceLineViews, traceViews);
    }
    else
    {
      return new LinkedHashSet<>();
    }
  }

  protected Set<TraceView> createTraceViews(Collection<Line> newTraceViewLines, Collection<TraceView> touchingTraceViews)
  {
    Set<Line> traceViewLines = new LinkedHashSet<>(newTraceViewLines);
    traceViewLines.addAll(getTraceViewLines(touchingTraceViews));
    removeTraceViews(touchingTraceViews);

    Set<Line> mergedLines = LineMinimiser.minimise(traceViewLines);
    Positions positionMap = new Positions(traceViewLines);

    LinePositionCache lineCache = new LinePositionCache(mergedLines);
    StaticViewIterator iterator = staticViewIterator();

    List<Int2D> additionalJunctions = new ArrayList<>();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      List<ConnectionView> connections = staticView.getConnectionViews();
      for (ConnectionView connection : connections)
      {
        Int2D position = connection.getGridPosition();
        if (lineCache.touchesLine(position.x, position.y))
        {
          additionalJunctions.add(position.clone());
        }
      }
    }

    Set<Line> splitLines = LineSplitter.split(lineCache, positionMap, additionalJunctions);

    Set<TraceView> traceViews = new LinkedHashSet<>();
    for (Line line : splitLines)
    {
      traceViews.add(new TraceView(this, line));
    }

    enableViews(traceViews);

    return traceViews;
  }

  public void createTracesForTraceViewsAndConnectionViews(ViewPath parentViewPath,
                                                          CircuitInstanceView circuitInstanceView,
                                                          Collection<ConnectionView> nonTraceViewConnectionViews,
                                                          Set<TraceView> traceViews)
  {
    Collection<ConnectionView> traceConnectionViews = findTraceViewConnectionViews(traceViews);
    if (traceConnectionViews.size() > 0)
    {
      traceConnectionViews.addAll(nonTraceViewConnectionViews);
    }
    else
    {
      traceConnectionViews = nonTraceViewConnectionViews;
    }

    if (traceConnectionViews.size() > 0)
    {
      Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViews(parentViewPath,
                                                                                  circuitInstanceView,
                                                                                  traceConnectionViews);
      fireConnectionEvents(updatedConnectionViews);
    }
  }

  protected Set<ConnectionView> findNonTraceViewConnectionViews(Set<TraceView> inputTraceViews)
  {
    Set<ConnectionView> nonTraceConnectionViews = new LinkedHashSet<>();
    int i = 0;
    for (TraceView traceView : inputTraceViews)
    {
      if (!traceView.hasConnections())
      {
        throw new SimulatorException("Cannot get a connection for a removed Trace.  Iteration [%s].", i);
      }

      ConnectionView connectionView = traceView.getStartConnection();
      List<View> connectedComponents = connectionView.getConnectedComponents();
      for (View connectedComponent : connectedComponents)
      {
        if (!(connectedComponent instanceof TraceView))
        {
          nonTraceConnectionViews.add(connectionView);
        }
      }

      connectionView = traceView.getEndConnection();
      connectedComponents = connectionView.getConnectedComponents();
      for (View connectedComponent : connectedComponents)
      {
        if (!(connectedComponent instanceof TraceView))
        {
          nonTraceConnectionViews.add(connectionView);
        }
      }
      i++;
    }
    return nonTraceConnectionViews;
  }

  protected Set<ConnectionView> findTraceViewConnectionViews(Set<TraceView> traceViews)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    int i = 0;
    for (TraceView traceView : traceViews)
    {
      if (!traceView.hasConnections())
      {
        throw new SimulatorException("Cannot get a connection for a removed Trace.  Iteration [%s].", i);
      }

      connectionViews.add(traceView.getStartConnection());
      connectionViews.add(traceView.getEndConnection());
      i++;
    }
    return connectionViews;
  }

  public SubcircuitEditorData save(Set<View> selection, long id)
  {
    ArrayList<StaticData<?>> staticDatas = new ArrayList<>();
    ArrayList<SubcircuitInstanceData> subcircuitInstanceDatas = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      StaticData<?> staticData = (StaticData<?>) staticView.save(selection.contains(staticView));
      if (staticData == null)
      {
        throw new SimulatorException("%s save may not return null.", staticView.toIdentifierString());
      }
      else if (staticData instanceof SubcircuitInstanceData)
      {
        subcircuitInstanceDatas.add((SubcircuitInstanceData) staticData);
      }
      else
      {
        staticDatas.add(staticData);
      }
    }

    ArrayList<TraceData> traceDatas = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      TraceData traceData = traceView.save(selection.contains(traceView));
      traceDatas.add(traceData);
    }

    SubcircuitData subcircuitData = new SubcircuitData(staticDatas, subcircuitInstanceDatas, traceDatas);
    return new SubcircuitEditorData(subcircuitData, typeName, id);
  }

  public void startMoveComponents(List<StaticView<?>> staticViews,
                                  List<TraceView> traceViews)
  {
    TraceViewConnectionViewSetsPair pair = findImmediateConnectedTraceViews(traceViews);
    LinkedHashSet<TraceView> connectedTraceViews = pair.getTraceViews();

    for (StaticView<?> staticView : staticViews)
    {
      if (staticView.isEnabled())
      {
        staticView.disable();
        disconnectStaticViewAndDestroyComponents(staticView);
      }
    }

    for (TraceView traceView : traceViews)
    {
      if (traceView.isEnabled())
      {
        traceView.disable();
        disconnectTraceViewAndDestroyComponents(traceView);
      }
    }

    LinkedHashSet<ConnectionView> nonTraceViewConnectionViews = pair.getNonTraceViewConnectionViews();
    Set<TraceView> newTraceViews = createTraceViews(new HashSet<>(), connectedTraceViews);
    createTracesForTraceViewsAndConnectionViews(circuitEditor.getViewPaths().getEmptyPath(),
                                                subcircuitEditor,
                                                nonTraceViewConnectionViews,
                                                newTraceViews);

    UpdatedViewPaths updatedPaths = circuitEditor.viewPathsUpdate();

    Set<SubcircuitView> updatedSubcircuitViews = updatedPaths.getSubcircuitViews();
    for (SubcircuitView subcircuitView : updatedSubcircuitViews)
    {
      subcircuitView.pathsUpdated(updatedPaths);
    }
  }

  public List<View> doneMoveComponents(List<StaticView<?>> staticViews,
                                       List<Line> newTraceViewLines,
                                       List<TraceView> removeTraceViews,
                                       Set<StaticView<?>> selectedViews)
  {
    removeTraceViews(new ArrayList<>(removeTraceViews));

    List<ConnectionView> connectionViews = getOrCreateStaticViewConnections(staticViews);
    TraceViewConnectionViewSetsPair pair = findTraceViewsInGridSpaceConnectionViewSets(connectionViews);
    Set<TraceView> existingTraceViews = pair.getTraceViews();
    Set<ConnectionView> nonTraceViewConnectionViews = pair.getNonTraceViewConnectionViews();

    List<Line> existingTraceViewLines = getTraceViewLines(existingTraceViews);
    removeTraceViews(existingTraceViews);

    existingTraceViewLines.addAll(newTraceViewLines);

    enableViews(staticViews);

    List<ComponentView<?>> componentViews = new ArrayList<>();
    List<SubcircuitInstanceView> subcircuitInstanceViews = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      if (staticView instanceof SubcircuitInstanceView)
      {
        subcircuitInstanceViews.add((SubcircuitInstanceView) staticView);
      }
      else if (staticView instanceof ComponentView)
      {
        componentViews.add((ComponentView) staticView);
      }
      else if (staticView instanceof TunnelView)
      {
      }
      else
      {
        throw new SimulatorException("Don't know how to create components for View of class [%s].", staticView.getClass().getSimpleName());
      }
    }

    List<ViewPathComponents> viewPathComponents = new ArrayList<>();
    ViewPathCircuitSimulation viewPathCircuitSimulation = new ViewPathCircuitSimulation(circuitEditor.getCurrentViewPath(),
                                                                                        circuitEditor.getCurrentCircuitSimulation());
    viewPathComponents.add(new ViewPathComponents(viewPathCircuitSimulation,
                                                  componentViews));

    List<SubcircuitInstanceCreation> creations = new ArrayList<>();
    for (ViewPath viewPath : viewPaths)
    {
      List<CircuitSimulation> circuitSimulationList = viewPath.getCircuitSimulation();
      for (CircuitSimulation circuitSimulation : circuitSimulationList)
      {
        recurseFindSubcircuitInstanceCreations(creations,
                                               viewPathComponents,
                                               viewPath,
                                               circuitSimulation,
                                               subcircuitInstanceViews
        );
      }
    }

    if (subcircuitInstanceViews.size() > 0)
    {
      circuitEditor.updateSimulationPaths();
    }

    createComponents(creations, viewPathComponents);

    for (SubcircuitInstanceCreation creation : creations)
    {
      SubcircuitView subcircuitView = creation.getSubcircuitInstanceView().getInstanceSubcircuitView();
      SubcircuitInstanceView subcircuitInstanceView = creation.getSubcircuitInstanceView();
      subcircuitView.createTracesForSubcircuitInstanceView(creation.getViewPath(), subcircuitInstanceView);
    }

    Set<TraceView> newTraceViews = createTraceViews(existingTraceViewLines);
    createTracesForTraceViewsAndConnectionViews(getCircuitEditor().getViewPaths().getEmptyPath(),
                                                subcircuitEditor,
                                                nonTraceViewConnectionViews,
                                                newTraceViews);

    Set<TraceView> selectedTraceViews = calculateSelectedTraceViews(newTraceViewLines, newTraceViews);
    return calculateNewSelection(staticViews,
                                 selectedViews,
                                 selectedTraceViews);
  }

  protected void recurseFindSubcircuitInstanceCreations(List<SubcircuitInstanceCreation> creations,
                                                        List<ViewPathComponents> viewPathComponentsList,
                                                        ViewPath viewPath,
                                                        CircuitSimulation circuitSimulation,
                                                        List<SubcircuitInstanceView> subcircuitInstanceViews)
  {
    List<SubcircuitInstanceCreation> localCreations = new ArrayList<>();

    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      SubcircuitInstanceCreation creation = subcircuitInstanceView.createComponentInSubcircuitInstanceCreation(viewPath, circuitSimulation);
      localCreations.add(creation);
    }

    ViewPaths viewPaths = getCircuitEditor().getViewPaths();

    for (SubcircuitInstanceCreation creation : localCreations)
    {
      SubcircuitInstanceView subcircuitInstanceView = creation.getSubcircuitInstanceView();
      SubcircuitView subcircuitView = subcircuitInstanceView.getInstanceSubcircuitView();

      ViewPath nextViewPath = viewPaths.getViewPath(viewPath, subcircuitInstanceView);
      ViewPathCircuitSimulation viewPathCircuitSimulation = new ViewPathCircuitSimulation(nextViewPath, circuitSimulation);
      viewPathComponentsList.add(new ViewPathComponents(viewPathCircuitSimulation, subcircuitView.getComponentViews()));
      subcircuitView.recurseFindSubcircuitInstanceCreations(creations,
                                                            viewPathComponentsList,
                                                            nextViewPath,
                                                            circuitSimulation,
                                                            subcircuitView.getSubcircuitInstanceViews());
    }

    creations.addAll(localCreations);
  }

  protected void createComponents(List<SubcircuitInstanceCreation> creations,
                                  List<ViewPathComponents> viewPathComponentsList)
  {
    for (ViewPathComponents viewPathComponents : viewPathComponentsList)
    {
      CircuitSimulation circuitSimulation = viewPathComponents.getCircuitSimulation();
      ViewPath viewPath = viewPathComponents.getViewPath();
      for (ComponentView<?> componentView : viewPathComponents.getComponentViews())
      {
        componentView.createComponent(viewPath, circuitSimulation);
      }
    }

    for (SubcircuitInstanceCreation creation : creations)
    {
      SubcircuitInstanceView subcircuitInstanceView = creation.getSubcircuitInstanceView();
      SubcircuitInstance subcircuitInstance = creation.getSubcircuitInstance();

      subcircuitInstanceView.createComponentsForSubcircuitInstanceView(creation.getViewPath(),
                                                                       creation.getCircuitSimulation(),
                                                                       subcircuitInstance);
    }

    for (SubcircuitInstanceCreation creation : creations)
    {
      SubcircuitInstanceView subcircuitInstanceView = creation.getSubcircuitInstanceView();
      CircuitSimulation circuitSimulation = creation.getCircuitSimulation();
      ViewPath viewPath = creation.getViewPath();
      subcircuitInstanceView.simulationStarted(viewPath, circuitSimulation);
    }

    for (ViewPathComponents viewPathComponents : viewPathComponentsList)
    {
      ViewPath viewPath = viewPathComponents.getViewPath();
      CircuitSimulation circuitSimulation = viewPathComponents.getCircuitSimulation();
      for (ComponentView<?> componentView : viewPathComponents.getComponentViews())
      {
        componentView.simulationStarted(viewPath, circuitSimulation);
      }
    }
  }

  protected Set<TraceView> calculateSelectedTraceViews(List<Line> newTraceViewLines, Collection<TraceView> traceViews)
  {
    Set<TraceView> selectedTraceViews = new LinkedHashSet<>();
    for (Line line : newTraceViewLines)
    {
      List<TraceOverlap> tracesTouching = getTracesTouching(line, traceViews);
      for (TraceOverlap traceOverlap : tracesTouching)
      {
        if (traceOverlap.isParallel())
        {
          TraceView traceView = traceOverlap.getTraceView();
          int overlap = getOverlap(line, traceView.getLine());
          if (overlap > 0)
          {
            selectedTraceViews.add(traceView);
          }
        }
      }
    }
    return selectedTraceViews;
  }

  protected TraceViewConnectionViewSetsPair findTraceViewsInGridSpaceConnectionViewSets(List<ConnectionView> connectionViews)
  {
    TraceViewConnectionViewSetsPair pair = new TraceViewConnectionViewSetsPair();
    for (ConnectionView connectionView : connectionViews)
    {
      Int2D position = connectionView.getGridPosition();
      List<TraceView> traceViewsInGridSpace = getTraceViewsInGridSpace(position.x, position.y);
      if (traceViewsInGridSpace.size() > 0)
      {
        pair.addAll(traceViewsInGridSpace);
      }
      else
      {
        pair.add(connectionView);
      }
    }
    return pair;
  }

  protected List<View> calculateNewSelection(List<StaticView<?>> staticViews,
                                             Set<StaticView<?>> selectedViews,
                                             Set<TraceView> newTraceViews)
  {
    List<View> newSelection = new ArrayList<>();
    for (StaticView<?> staticView : staticViews)
    {
      if (selectedViews.contains(staticView))
      {
        newSelection.add(staticView);
      }
    }
    newSelection.addAll(newTraceViews);

    return newSelection;
  }

  protected TraceViewConnectionViewSetsPair findImmediateConnectedTraceViews(List<TraceView> traceViews)
  {
    Set<TraceView> traceViewSet = new HashSet<>(traceViews);
    TraceViewConnectionViewSetsPair pair = new TraceViewConnectionViewSetsPair();

    for (TraceView traceView : traceViews)
    {
      List<ConnectionView> connectionViews = traceView.getConnectionViews();
      for (ConnectionView connectionView : connectionViews)
      {
        findImmediateConnectedTraceViews(traceViewSet, pair, connectionView);
      }
    }

    return pair;
  }

  private void findImmediateConnectedTraceViews(Set<TraceView> traceViewSet,
                                                TraceViewConnectionViewSetsPair pair,
                                                ConnectionView connectionView)
  {
    List<View> connectedComponents = connectionView.getConnectedComponents();
    for (View connectedComponent : connectedComponents)
    {
      if (connectedComponent instanceof TraceView)
      {
        TraceView connectedTraceView = (TraceView) connectedComponent;
        if (!traceViewSet.contains(connectedTraceView))
        {
          pair.add(connectedTraceView);
        }
      }
      else
      {
        pair.add(connectionView);
      }
    }
  }

  public List<View> getSelectionFromRectangle(Float2D start,
                                              Float2D end)
  {
    boolean includeIntersections = start.x > end.x;

    Float2D boundBoxPosition = new Float2D();
    Float2D boundBoxDimension = new Float2D();
    List<View> selectedViews = new ArrayList<>();
    StaticViewIterator iterator = staticViewIterator();
    while (iterator.hasNext())
    {
      StaticView<?> staticView = iterator.next();
      updateSelectedViews(start,
                          end,
                          includeIntersections,
                          boundBoxPosition,
                          boundBoxDimension,
                          selectedViews,
                          staticView);
    }

    for (TraceView traceView : traceViews)
    {
      Line line = traceView.getLine();
      line.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
      if (isPoint(start, end))
      {
        if (line.isPositionOn(new Int2D(start)))
        {
          selectedViews.add(traceView);
        }
      }
      else
      {
        if (BoundingBox.containsBox(start, end, boundBoxPosition, boundBoxDimension, includeIntersections))
        {
          selectedViews.add(traceView);
        }
      }
    }
    return selectedViews;
  }

  protected void updateSelectedViews(Float2D start,
                                     Float2D end,
                                     boolean includeIntersections,
                                     Float2D boundBoxPosition,
                                     Float2D boundBoxDimension,
                                     List<View> selectedViews,
                                     StaticView<?> componentView)
  {
    if (componentView.isEnabled())
    {
      componentView.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
      if (isPoint(start, end))
      {
        if (BoundingBox.containsPoint(new Int2D(start), boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(componentView);
        }
      }
      else
      {
        if (BoundingBox.containsBox(start, end, boundBoxPosition, boundBoxDimension, includeIntersections))
        {
          selectedViews.add(componentView);
        }
      }
    }
  }

  protected boolean isPoint(Float2D start, Float2D end)
  {
    return (Math.round(start.x) == Math.round(end.x)) &&
           (Math.round(start.y) == Math.round(end.y));
  }

  public String getTypeName()
  {
    return typeName;
  }

  public void setTypeName(String typeName)
  {
    this.typeName = typeName;
  }

  @Override
  public String toString()
  {
    return typeName;
  }

  public List<PinView> findAllPins()
  {
    ArrayList<PinView> pinViews = new ArrayList<>();
    for (PassiveView<?, ?> passiveView : passiveViews)
    {
      if (passiveView instanceof PinView)
      {
        pinViews.add((PinView) passiveView);
      }
    }
    return pinViews;
  }

  public Set<SubcircuitInstanceView> findAllSubcircuitInstanceViews()
  {
    return subcircuitInstanceViews;
  }

  public Set<StaticView<?>> findAllViewsOfClass(Class<? extends StaticView<?>> viewClass)
  {
    Set<StaticView<?>> views = new LinkedHashSet<>();
    if (DecorativeView.class.isAssignableFrom(viewClass))
    {
      for (DecorativeView<?> decorativeView : decorativeViews)
      {
        if (viewClass.isAssignableFrom(decorativeView.getClass()))
        {
          views.add(decorativeView);
        }
      }
    }
    else if (IntegratedCircuitView.class.isAssignableFrom(viewClass))
    {
      for (IntegratedCircuitView<?, ?> integratedCircuitView : integratedCircuitViews)
      {
        if (viewClass.isAssignableFrom(integratedCircuitView.getClass()))
        {
          views.add(integratedCircuitView);
        }
      }
    }
    else if (PassiveView.class.isAssignableFrom(viewClass))
    {
      for (PassiveView<?, ?> passiveView : passiveViews)
      {
        if (viewClass.isAssignableFrom(passiveView.getClass()))
        {
          views.add(passiveView);
        }
      }
    }
    else if (SubcircuitInstanceView.class.isAssignableFrom(viewClass))
    {
      for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
      {
        if (viewClass.isAssignableFrom(subcircuitInstanceView.getClass()))
        {
          views.add(subcircuitInstanceView);
        }
      }
    }
    return views;
  }

  public List<String> getSubcircuitInstanceNames()
  {
    Set<String> names = new HashSet<>();
    Set<SubcircuitInstanceView> subcircuitInstanceViews = findAllSubcircuitInstanceViews();
    for (SubcircuitInstanceView subcircuitInstanceView : subcircuitInstanceViews)
    {
      names.add(subcircuitInstanceView.getTypeName());
    }
    return new ArrayList<>(names);
  }

  public List<String> getTypeNameAsList()
  {
    ArrayList<String> list = new ArrayList<>();
    list.add(getTypeName());
    return list;
  }

  public void destroySubcircuitInstanceComponentsAndSimulations(ViewPath viewPath, CircuitSimulation circuitSimulation)
  {
    destroyComponents(getStaticViews(),
                      traceViews,
                      viewPath,
                      circuitSimulation);

    SubcircuitSimulation subcircuitSimulation = viewPath.getSubcircuitSimulation(circuitSimulation);
    simulations.remove(subcircuitSimulation);
  }

  protected void destroyComponents(Collection<StaticView<?>> staticViews,
                                   Collection<TraceView> traceViews,
                                   ViewPath viewPath,
                                   CircuitSimulation circuitSimulation)
  {
    for (StaticView<?> staticView : staticViews)
    {
      if (!(staticView instanceof SubcircuitInstanceView))
      {
        staticView.destroyComponent(viewPath, circuitSimulation);
      }
    }

    for (TraceView traceView : traceViews)
    {
      traceView.destroyComponent(viewPath, circuitSimulation);
    }

    for (StaticView<?> staticView : staticViews)
    {
      if (staticView instanceof SubcircuitInstanceView)
      {
        staticView.destroyComponent(viewPath, circuitSimulation);
      }
    }
  }

  public void createTracesForSubcircuitInstanceView(ViewPath parentViewPath, SubcircuitInstanceView subcircuitInstanceView)
  {
    List<StaticView<?>> staticViews = getStaticViews();
    Set<ConnectionView> connectionViews = getConnectionViews(staticViews, traceViews);

    Set<ConnectionView> updatedConnectionViews = createTracesForConnectionViews(parentViewPath,
                                                                                subcircuitInstanceView,
                                                                                connectionViews);
    fireConnectionEvents(updatedConnectionViews);
  }

  public CircuitSimulation createSubcircuitTopSimulation(String name)
  {
    CircuitSimulation circuitSimulation = new CircuitSimulation(name);
    SubcircuitTopSimulation subcircuitTopSimulation = new SubcircuitTopSimulation(circuitSimulation);
    circuitSimulation.setTopSimulation(subcircuitTopSimulation);
    addSubcircuitSimulation(subcircuitTopSimulation);
    return circuitSimulation;
  }

  public List<SubcircuitSimulation> getSubcircuitSimulations(CircuitSimulation circuitSimulation)
  {
    return simulations.getSubcircuitSimulations(circuitSimulation);
  }

  public List<? extends SubcircuitSimulation> getSubcircuitSimulations()
  {
    return simulations.getSubcircuitSimulations();
  }

  public SubcircuitSimulations getSimulations()
  {
    return simulations;
  }

  public List<CircuitSimulation> getCircuitSimulations()
  {
    return new ArrayList<>(simulations.getCircuitSimulations());
  }

  public void addSubcircuitSimulation(SubcircuitSimulation subcircuitSimulation)
  {
    simulations.add(subcircuitSimulation);
  }

  public void validateSimulations(List<CircuitInstanceView> orderedTopDownCircuitInstanceViews)
  {
    Collection<SubcircuitTopSimulation> subcircuitTopSimulations = simulations.getSubcircuitTopSimulations();
    if ((subcircuitTopSimulations.isEmpty()))
    {
      throw new SimulatorException("SubcircuitView [%s] expected at least one subcircuit top simulation.", getTypeName());
    }

    if (orderedTopDownCircuitInstanceViews.size() == 0)
    {
      throw new SimulatorException("SubcircuitView [%s] expected at least one circuit instance view.", getTypeName());
    }
    CircuitInstanceView circuitInstanceView = orderedTopDownCircuitInstanceViews.get(0);
    if (circuitInstanceView.getInstanceSubcircuitView() != this)
    {
      throw new SimulatorException("SubcircuitView [%s] does not match first circuit instance view [%s].", getTypeName(), circuitInstanceView.getInstanceSubcircuitView().getTypeName());
    }

    simulations.validate(orderedTopDownCircuitInstanceViews);
  }

  public CircuitSimulation addNewSimulation(String simulationName)
  {
    throw new SimulatorException();
  }

  public CircuitEditor getCircuitEditor()
  {
    return circuitEditor;
  }

  public List<ViewPath> getViewPaths(CircuitSimulation circuitSimulation)
  {
    List<ViewPath> result = new ArrayList<>();
    for (ViewPath viewPath : viewPaths)
    {
      if (viewPath.containsCircuitSimulation(circuitSimulation))
      {
        result.add(viewPath);
      }
    }
    return result;
  }

  public PathConnectionView getPathConnection(ViewPath viewPath, ConnectionView connectionView)
  {
    PathConnectionView pathConnectionView = connectionViewCache.getPathConnectionView(viewPath, connectionView);
    if (pathConnectionView != null)
    {
      return pathConnectionView;
    }
    else
    {
      throw new SimulatorException("Cannot get Path Connection View for Path [%s] and Connection [%s].",
                                   viewPath.toString(),
                                   connectionView.toString());
    }
  }

  public CircuitInstanceView getSubcircuitEditor()
  {
    return subcircuitEditor;
  }

  public void setSubcircuitEditor(CircuitInstanceView subcircuitEditor)
  {
    this.subcircuitEditor = subcircuitEditor;
  }

  public void setViewPaths(List<ViewPath> viewPaths)
  {
    if (!this.viewPaths.isEmpty())
    {
      throw new SimulatorException("View Paths must be empty.");
    }

    this.viewPaths = viewPaths;
    connectionViewCache.addPaths(viewPaths);
  }
}

