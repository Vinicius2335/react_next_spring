export interface Marca {
	id: number;
	dataCriacao: string;
	dataAtualizacao: string;
	nome: string;
}

export interface Categoria {
	id: number;
	dataCriacao: string;
	dataAtualizacao: string;
	nome: string;
}

export interface Produto {
	id: number;
	dataCriacao: string;
	dataAtualizacao: string;
	quantidade: number;
	descricaoCurta: string;
	descricaoDetalhada: string;
	valorCusto: number;
	valorVenda: number;
	marca: Marca;
	categoria: Categoria;
}

export interface ProdutoImagemResponse {
	id: number;
	dataCriacao: string;
	dataAtualizacao: string;
	nome: string;
	imageCode: string;
	arquivo: string;
	produto: Produto;
}