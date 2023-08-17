import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'brand',
        data: { pageTitle: 'controTxApp.brand.home.title' },
        loadChildren: () => import('./brand/brand.module').then(m => m.BrandModule),
      },
      {
        path: 'operator',
        data: { pageTitle: 'controTxApp.operator.home.title' },
        loadChildren: () => import('./operator/operator.module').then(m => m.OperatorModule),
      },
      {
        path: 'contract',
        data: { pageTitle: 'controTxApp.contract.home.title' },
        loadChildren: () => import('./contract/contract.module').then(m => m.ContractModule),
      },
      {
        path: 'interface-board',
        data: { pageTitle: 'controTxApp.interfaceBoard.home.title' },
        loadChildren: () => import('./interface-board/interface-board.module').then(m => m.InterfaceBoardModule),
      },
      {
        path: 'control-interface-board',
        data: { pageTitle: 'controTxApp.controlInterfaceBoard.home.title' },
        loadChildren: () => import('./control-interface-board/control-interface-board.module').then(m => m.ControlInterfaceBoardModule),
      },
      {
        path: 'purchase-order',
        data: { pageTitle: 'controTxApp.purchaseOrder.home.title' },
        loadChildren: () => import('./purchase-order/purchase-order.module').then(m => m.PurchaseOrderModule),
      },
      {
        path: 'reception-order',
        data: { pageTitle: 'controTxApp.receptionOrder.home.title' },
        loadChildren: () => import('./reception-order/reception-order.module').then(m => m.ReceptionOrderModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
