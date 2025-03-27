import { Component } from '@angular/core';

@Component({
  selector: 'app-dummy',
  standalone: true,
  imports: [],
  templateUrl: './dummy.component.html',
  styleUrl: './dummy.component.css'
})
export class DummyComponent {
  constructor() {
    console.log("Dummy Component loaded");
  }
}
