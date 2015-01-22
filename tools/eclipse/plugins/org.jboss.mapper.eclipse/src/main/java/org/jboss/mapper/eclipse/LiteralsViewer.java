package org.jboss.mapper.eclipse;

import java.util.Iterator;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.mapper.Literal;
import org.jboss.mapper.dozer.ConfigBuilder;

class LiteralsViewer extends Composite {

    LiteralsViewer( Composite parent,
                     final ConfigBuilder configBuilder ) {
        super( parent, SWT.NONE );

        setLayout( GridLayoutFactory.fillDefaults().create() );
        setBackground( parent.getBackground() );

        final ToolBar toolBar = new ToolBar( this, SWT.NONE );
        final ToolItem addButton = new ToolItem( toolBar, SWT.PUSH );
        addButton.setImage( PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_OBJ_ADD ) );
        addButton.setToolTipText( "Add a new literal" );
        final ToolItem deleteButton = new ToolItem( toolBar, SWT.PUSH );
        deleteButton.setImage( PlatformUI.getWorkbench().getSharedImages().getImage( ISharedImages.IMG_ETOOL_DELETE ) );
        deleteButton.setToolTipText( "Delete the selected literal(s)" );
        deleteButton.setEnabled( false );

        final ListViewer listViewer = new ListViewer( this );
        listViewer.getList().setLayoutData( GridDataFactory.fillDefaults().grab( true, true ).create() );
        listViewer.addDragSupport( DND.DROP_MOVE,
                                        new Transfer[] { LocalSelectionTransfer.getTransfer() },
                                        new DragSourceAdapter() {

            @Override
            public void dragSetData( final DragSourceEvent event ) {
                if ( LocalSelectionTransfer.getTransfer().isSupportedType( event.dataType ) )
                    LocalSelectionTransfer.getTransfer().setSelection( listViewer.getSelection() );
            }
        } );
        listViewer.setComparator( new ViewerComparator() );
        listViewer.setLabelProvider( new ColumnLabelProvider() {

            @Override
            public String getText( final Object element ) {
                final Literal literal = ( Literal ) element;
                return literal.getValue();
            }
        } );
        addButton.addSelectionListener( new SelectionAdapter() {

            @Override
            public void widgetSelected( SelectionEvent event ) {
                final InputDialog dlg = new InputDialog( getShell(),
                                                         "Add Literal",
                                                         "Enter a new literal value",
                                                         null,
                                                         new IInputValidator() {

                                                             @Override
                                                             public String isValid( String text ) {
                                                                 return listViewer.getList().indexOf( text ) < 0 ? null : "Value already exists";
                                                             }
                                                         } );
                if ( dlg.open() == Window.OK ) listViewer.add( dlg.getValue() );
            }
        } );
        listViewer.addSelectionChangedListener( new ISelectionChangedListener() {

            @Override
            public void selectionChanged( SelectionChangedEvent event ) {
                deleteButton.setEnabled( !event.getSelection().isEmpty() );
            }
        } );
        deleteButton.addSelectionListener( new SelectionAdapter() {

            @Override
            public void widgetSelected( SelectionEvent event ) {
                for ( final Iterator< ? > iter = ( ( IStructuredSelection ) listViewer.getSelection() ).iterator(); iter.hasNext(); ) {
                    listViewer.remove( iter.next() );
                }
            }
        } );
        // Initialize literals from config
        listViewer.add( configBuilder.getLiterals().toArray() );
}
}